package co.binary.exploregithubandroid.feature.home.user

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.binary.exploregithubandroid.core.domain.GetGitHubUserDetailUseCase
import co.binary.exploregithubandroid.core.model.GitHubUserDetail
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
    data class Success(val user: GitHubUserDetail) : GitHubUserDetailUiState
    data object Error : GitHubUserDetailUiState
}

private const val TAG = "GitHubUserDetailViewModel"

@HiltViewModel
internal class GitHubUserDetailViewModel @Inject constructor(
    private val getGitHubUserDetailUseCase: GetGitHubUserDetailUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val args = UserDetailArgs(savedStateHandle)

    private val _uiState = MutableStateFlow<GitHubUserDetailUiState>(GitHubUserDetailUiState.Loading)
    val uiState: StateFlow<GitHubUserDetailUiState> = _uiState.asStateFlow()

    init {
        getGitHubUserDetail(username = args.username)
    }

    fun getGitHubUserDetail(username: String) {
        viewModelScope.launch {
            getGitHubUserDetailUseCase(username).fold(
                onSuccess = {
                    GitHubUserDetailUiState.Success(it)
                },
                onFailure = {
                    Log.e(TAG, "getGitHubUserDetail: ", it)
                    GitHubUserDetailUiState.Error
                }
            ).let { newState ->
                _uiState.update { newState }
            }
        }
    }
}
