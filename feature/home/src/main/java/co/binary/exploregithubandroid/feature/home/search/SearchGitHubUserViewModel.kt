package co.binary.exploregithubandroid.feature.home.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.binary.exploregithubandroid.core.domain.user.ClearRecentSearchUseCase
import co.binary.exploregithubandroid.core.domain.user.GetRecentSearchStreamUseCase
import co.binary.exploregithubandroid.core.domain.user.InsertOrReplaceRecentSearchUseCase
import co.binary.exploregithubandroid.core.domain.user.SearchGitHubUsersUseCase
import co.binary.exploregithubandroid.core.model.user.GitHubUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SearchGitHubUserUiState {
    val queryText: String

    data class Initial(override val queryText: String = "") : SearchGitHubUserUiState

    data class Loading(override val queryText: String = "") : SearchGitHubUserUiState
    data class Success(
        override val queryText: String,
        val users: List<GitHubUser>,
        val shouldLoadMore: Boolean,
        val loadingMore: Boolean = false,
        val error: Throwable? = null,
        val isSearching: Boolean = false,
    ) : SearchGitHubUserUiState

    data class Empty(override val queryText: String = "") : SearchGitHubUserUiState

    data class Error(override val queryText: String = "", val error: Throwable) : SearchGitHubUserUiState
}

private data class SearchGitHubUserViewModelState(
    val loading: Boolean = false,
    val users: List<GitHubUser> = emptyList(),
    val queryText: String = "",
    val error: Throwable? = null,
    val page: Int = 1,
    val loadingMore: Boolean = false,
    val allLoaded: Boolean = false,
    val isSearching: Boolean = false,
) {
    fun clearPrevSearchState() = copy(
        users = emptyList(),
        error = null,
        page = 1,
        allLoaded = false,
    )

    fun toUiState(): SearchGitHubUserUiState = when {
        queryText.isEmpty() -> SearchGitHubUserUiState.Initial(queryText)
        loading -> SearchGitHubUserUiState.Loading(queryText)
        users.isEmpty() && error != null -> SearchGitHubUserUiState.Error(queryText, error)
        users.isEmpty() && queryText.isNotEmpty() -> SearchGitHubUserUiState.Empty(queryText)
        else -> SearchGitHubUserUiState.Success(
            queryText = queryText,
            users = users,
            shouldLoadMore = !allLoaded,
            loadingMore = loadingMore,
            error = error,
            isSearching = isSearching,
        )
    }
}

data class SearchBarUiState(
    val isSearching: Boolean = false,
    val searchBarText: String = "",
    val recentSearches: List<String> = emptyList(),
)

private data class SearchBarViewModelState(
    val isSearching: Boolean = false,
    val searchBarText: String = "",
)

@HiltViewModel
internal class SearchGitHubUserViewModel @Inject constructor(
    private val searchGitHubUserUseCase: SearchGitHubUsersUseCase,
    private val recentSearchStreamUseCase: GetRecentSearchStreamUseCase,
    private val insertOrReplaceRecentSearchUseCase: InsertOrReplaceRecentSearchUseCase,
    private val clearRecentSearchUseCase: ClearRecentSearchUseCase,
) : ViewModel() {

    private val viewModelState = MutableStateFlow(SearchGitHubUserViewModelState())
    val uiState: StateFlow<SearchGitHubUserUiState> = viewModelState
        .map { it.toUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, SearchGitHubUserViewModelState().toUiState())

    private val searchBarViewModelState = MutableStateFlow(SearchBarViewModelState())

    val searchBarUiState: StateFlow<SearchBarUiState> =
        combine(searchBarViewModelState, recentSearchStreamUseCase()) { state, recentSearches ->
            SearchBarUiState(
                isSearching = state.isSearching,
                searchBarText = state.searchBarText,
                recentSearches = recentSearches,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SearchBarUiState(),
        )

    fun search(searchText: String) {
        viewModelScope.launch {
            // Validate input
            val text = searchText.trim()
            // If empty, no need to search. Clear the list and return
            if (text.isEmpty()) {
                viewModelState.update {
                    it.clearPrevSearchState().copy(queryText = "")
                }
                return@launch
            }

            viewModelState.update {
                it.clearPrevSearchState().copy(loading = true, queryText = text)
            }
            val state = viewModelState.value
            launch {
                insertOrReplaceRecentSearchUseCase(state.queryText)
            }
            launch {
                searchGitHubUserUseCase(state.queryText, state.page).fold(
                    onSuccess = { data ->
                        viewModelState.update { it.copy(loading = false, users = data) }
                    },
                    onFailure = { throwable ->
                        viewModelState.update { it.copy(loading = false, error = throwable) }
                    }
                )
            }
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            viewModelState.update { it.copy(loadingMore = true) }
            val state = viewModelState.value
            val newPage = state.page + 1
            searchGitHubUserUseCase(state.queryText, newPage).fold(
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

    fun toggleSearch() {
        searchBarViewModelState.update {
            if (it.isSearching) {
                it.copy(isSearching = false, searchBarText = "")
            } else {
                it.copy(isSearching = true, searchBarText = viewModelState.value.queryText)
            }
        }
    }

    fun updateSearchText(text: String) {
        searchBarViewModelState.update {
            it.copy(searchBarText = text)
        }
    }

    fun clearSearchText() {
        viewModelScope.launch {
            viewModelState.update { it.clearPrevSearchState().copy(queryText = "") }
        }
    }

    fun clearRecentSearches() {
        viewModelScope.launch {
            clearRecentSearchUseCase()
        }
    }
}
