package co.binary.exploregithubandroid.feature.home.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.binary.exploregithubandroid.core.domain.SearchGitHubUsersUseCase
import co.binary.exploregithubandroid.core.model.GitHubUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface SearchGitHubUsersUiState {
    data object Initial : SearchGitHubUsersUiState
    data object Loading : SearchGitHubUsersUiState
    data class Success(val users: List<GitHubUser>) : SearchGitHubUsersUiState
    data object Empty: SearchGitHubUsersUiState
    data object Error : SearchGitHubUsersUiState
}

private const val TAG = "SearchGitHubUserViewModel"

@HiltViewModel
internal class SearchGitHubUserViewModel @Inject constructor(
    private val searchGitHubUserUseCase: SearchGitHubUsersUseCase,
): ViewModel() {

    private val _uiState = MutableStateFlow<SearchGitHubUsersUiState>(SearchGitHubUsersUiState.Initial)
    val uiState: StateFlow<SearchGitHubUsersUiState> = _uiState.asStateFlow()

    init {
        search("johndoe")
    }

    fun search(query: String) {
        viewModelScope.launch {
            searchGitHubUserUseCase(query).fold(
                onSuccess = {
                    Log.d(TAG, "search: success")
                    if (it.isEmpty()) {
                        SearchGitHubUsersUiState.Empty
                    } else {
                        SearchGitHubUsersUiState.Success(it)
                    }
                },
                onFailure = {
                    Log.d(TAG, "search: error")
                    SearchGitHubUsersUiState.Error
                }
            ).let { newState ->
                _uiState.update { newState }
            }
        }
    }
}
