package co.binary.exploregithubandroid.feature.home.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.binary.exploregithubandroid.core.domain.user.GetGitHubUserDetailUseCase
import co.binary.exploregithubandroid.core.domain.user.GetGitHubUserReposUseCase
import co.binary.exploregithubandroid.core.model.user.GitHubUserDetail
import co.binary.exploregithubandroid.feature.home.navigation.UserDetailArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface GitHubUserDetailUiState {
    data object Loading : GitHubUserDetailUiState
    data class Success(
        val user: GitHubUserDetail,
        val page: Int,
        val loadingMore: Boolean = false,
        val allLoaded: Boolean = false,
        val error: Throwable? = null,
    ) : GitHubUserDetailUiState

    data object Error : GitHubUserDetailUiState
}

@HiltViewModel
internal class GitHubUserDetailViewModel @Inject constructor(
    private val getGitHubUserDetailUseCase: GetGitHubUserDetailUseCase,
    private val getGitHubUserReposUseCase: GetGitHubUserReposUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val args = UserDetailArgs(savedStateHandle)

    private val _uiState = MutableStateFlow<GitHubUserDetailUiState>(GitHubUserDetailUiState.Loading)
    val uiState: StateFlow<GitHubUserDetailUiState> = _uiState.asStateFlow()

    init {
        getGitHubUserDetail(username = args.username)
    }

    private fun getGitHubUserDetail(username: String) {
        viewModelScope.launch {
            val page = 1
            getGitHubUserDetailUseCase(username = username, page = page).fold(
                onSuccess = { data ->
                    GitHubUserDetailUiState.Success(user = data, page = page)
                },
                onFailure = {
                    GitHubUserDetailUiState.Error
                }
            ).let { newState ->
                _uiState.update { newState }
            }
        }
    }

    fun loadMore() {
        val state = (uiState.value as? GitHubUserDetailUiState.Success) ?: return
        _uiState.update { state.copy(error = null, loadingMore = true) }
        viewModelScope.launch {
            val newPage = state.page + 1
            getGitHubUserReposUseCase(username = args.username, page = newPage).fold(
                onSuccess = { newData ->
                    if (newData.isEmpty()) {
                        state.copy(allLoaded = true)
                    } else {
                        val user = state.user.copy(repos = state.user.repos + newData)
                        state.copy(user = user, page = newPage)
                    }
                },
                onFailure = { throwable ->
                    state.copy(error = throwable)
                }
            ).let { newState ->
                _uiState.update { newState.copy(loadingMore = false) }
            }
        }
    }
}
