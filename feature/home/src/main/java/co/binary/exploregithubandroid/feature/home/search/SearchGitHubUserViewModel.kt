package co.binary.exploregithubandroid.feature.home.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.binary.exploregithubandroid.core.domain.SearchGitHubUsersUseCase
import co.binary.exploregithubandroid.core.model.GitHubUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SearchGitHubUsersUiState {
    val searchText: String

    data class Initial(override val searchText: String = "") : SearchGitHubUsersUiState

    data class Loading(override val searchText: String = "") : SearchGitHubUsersUiState
    data class Success(
        override val searchText: String,
        val users: List<GitHubUser>,
        val loadingMore: Boolean = false,
        val error: Boolean = false,
    ) : SearchGitHubUsersUiState

    data class Empty(override val searchText: String = "") : SearchGitHubUsersUiState

    data class Error(override val searchText: String = "") : SearchGitHubUsersUiState
}

private data class SearchGitHubUserViewModelState(
    val loading: Boolean = false,
    val users: List<GitHubUser> = emptyList(),
    val searchText: String = "",
    val error: Boolean = false,
    val page: Int = 1,
    val loadingMore: Boolean = false,
) {
    // FIXME: Fix states later
    fun toUiState(): SearchGitHubUsersUiState = when {
        loading -> SearchGitHubUsersUiState.Loading(searchText)
        users.isNotEmpty() -> SearchGitHubUsersUiState.Success(
            searchText = searchText,
            users = users,
            loadingMore = loadingMore,
            error = error,
        )
        error -> SearchGitHubUsersUiState.Error(searchText)
        else -> SearchGitHubUsersUiState.Initial(searchText)
    }
}

private const val TAG = "SearchGitHubUserViewModel"

@HiltViewModel
internal class SearchGitHubUserViewModel @Inject constructor(
    private val searchGitHubUserUseCase: SearchGitHubUsersUseCase,
) : ViewModel() {

    private val viewModelState = MutableStateFlow(SearchGitHubUserViewModelState())
    val uiState: StateFlow<SearchGitHubUsersUiState> = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, SearchGitHubUserViewModelState().toUiState())

    fun search() {
        viewModelScope.launch {
            viewModelState.update { it.copy(loading = true, users = emptyList(), error = false, page = 1) }
            val state = viewModelState.value
            searchGitHubUserUseCase(state.searchText, state.page).fold(
                onSuccess = { data ->
                    Log.d(TAG, "search: success")
                    viewModelState.update { it.copy(loading = false, users = data) }
                },
                onFailure = { throwable ->
                    Log.e(TAG, "search: error", throwable)
                    viewModelState.update { it.copy(loading = false, error = true) }
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
                    Log.d(TAG, "loadMore: success")
                    if (newData.isEmpty()) {
                        // No more data
                        // SearchGitHubUsersUiState.Empty
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
                onFailure = {
                    Log.d(TAG, "loadMore: error")
                    viewModelState.update { it.copy(loadingMore = false, error = true) }
                }
            )
        }
    }

    fun updateSearchText(text: String) {
        viewModelState.update { it.copy(searchText = text) }
    }

    fun clearSearchText() {
        viewModelState.update { it.copy(searchText = "") }
    }
}
