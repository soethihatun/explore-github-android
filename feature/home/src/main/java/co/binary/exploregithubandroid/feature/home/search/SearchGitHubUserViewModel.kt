package co.binary.exploregithubandroid.feature.home.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.binary.exploregithubandroid.core.domain.user.SearchGitHubUsersUseCase
import co.binary.exploregithubandroid.core.model.user.GitHubUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SearchGitHubUserUiState {
    val searchText: String

    data class Initial(override val searchText: String = "") : SearchGitHubUserUiState

    data class Loading(override val searchText: String = "") : SearchGitHubUserUiState
    data class Success(
        override val searchText: String,
        val users: List<GitHubUser>,
        val shouldLoadMore: Boolean,
        val loadingMore: Boolean = false,
        val error: Throwable? = null,
    ) : SearchGitHubUserUiState

    data class Empty(override val searchText: String = "") : SearchGitHubUserUiState

    data class Error(override val searchText: String = "", val error: Throwable) : SearchGitHubUserUiState
}

private data class SearchGitHubUserViewModelState(
    val loading: Boolean = false,
    val users: List<GitHubUser> = emptyList(),
    val searchText: String = "",
    val error: Throwable? = null,
    val page: Int = 1,
    val loadingMore: Boolean = false,
    val allLoaded: Boolean = false,
) {
    fun clearPrevSearchState() = copy(
        users = emptyList(),
        error = null,
        page = 1,
        allLoaded = false,
    )

    fun toUiState(): SearchGitHubUserUiState = when {
        searchText.isEmpty() -> SearchGitHubUserUiState.Initial(searchText)
        loading -> SearchGitHubUserUiState.Loading(searchText)
        users.isEmpty() && error != null -> SearchGitHubUserUiState.Error(searchText, error)
        users.isEmpty() && searchText.isNotEmpty() -> SearchGitHubUserUiState.Empty(searchText)
        else -> SearchGitHubUserUiState.Success(
            searchText = searchText,
            users = users,
            shouldLoadMore = !allLoaded,
            loadingMore = loadingMore,
            error = error,
        )
    }
}

@HiltViewModel
internal class SearchGitHubUserViewModel @Inject constructor(
    private val searchGitHubUserUseCase: SearchGitHubUsersUseCase,
) : ViewModel() {

    private val viewModelState = MutableStateFlow(SearchGitHubUserViewModelState())
    val uiState: StateFlow<SearchGitHubUserUiState> = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, SearchGitHubUserViewModelState().toUiState())

    fun search(searchText: String) {
        viewModelScope.launch {
            // Validate input
            val text = searchText.trim()
            // If empty, no need to search. Clear the list and return
            if (text.isEmpty()) {
                viewModelState.update {
                    it.clearPrevSearchState().copy(searchText = "")
                }
                return@launch
            }

            viewModelState.update {
                it.clearPrevSearchState().copy(loading = true, searchText = text)
            }
            val state = viewModelState.value
            searchGitHubUserUseCase(state.searchText, state.page).fold(
                onSuccess = { data ->
                    viewModelState.update { it.copy(loading = false, users = data) }
                },
                onFailure = { throwable ->
                    viewModelState.update { it.copy(loading = false, error = throwable) }
                }
            )
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            viewModelState.update { it.copy(loadingMore = true) }
            val state = viewModelState.value
            val newPage = state.page + 1
            searchGitHubUserUseCase(state.searchText, newPage).fold(
                onSuccess = { newData ->
                    if (newData.isEmpty()) {
                        // No more data
                        viewModelState.update { it.copy(loadingMore = false, allLoaded = true) }
                    } else {
                        viewModelState.update {
                            it.copy(
                                loadingMore = false,
                                users = state.users + newData,
                                page = newPage,
                            )
                        }
                    }
                },
                onFailure = { throwable ->
                    viewModelState.update { it.copy(loadingMore = false, error = throwable) }
                }
            )
        }
    }
}
