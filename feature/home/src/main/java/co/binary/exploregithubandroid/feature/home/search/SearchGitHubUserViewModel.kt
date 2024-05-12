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

sealed interface SearchUserUiState {
    data object Initial : SearchUserUiState
    data object Loading : SearchUserUiState
    data class Success(val users: List<GitHubUser>) : SearchUserUiState
    data object Empty: SearchUserUiState
    data object Error : SearchUserUiState
}

private const val TAG = "SearchGitHubUserViewModel"

@HiltViewModel
internal class SearchGitHubUserViewModel @Inject constructor(
    private val searchGitHubUserUseCase: SearchGitHubUsersUseCase,
): ViewModel() {

    private val _uiState = MutableStateFlow<SearchUserUiState>(SearchUserUiState.Initial)
    val uiState: StateFlow<SearchUserUiState> = _uiState.asStateFlow()

    init {
        search("johndoe")
    }

    fun search(query: String) {
        viewModelScope.launch {
            searchGitHubUserUseCase(query).fold(
                onSuccess = {
                    Log.d(TAG, "search: success")
                    if (it.isEmpty()) {
                        SearchUserUiState.Empty
                    } else {
                        SearchUserUiState.Success(it)
                    }
                },
                onFailure = {
                    Log.d(TAG, "search: error")
                    SearchUserUiState.Error
                }
            ).let { newState ->
                _uiState.update { newState }
            }
        }
    }
}
