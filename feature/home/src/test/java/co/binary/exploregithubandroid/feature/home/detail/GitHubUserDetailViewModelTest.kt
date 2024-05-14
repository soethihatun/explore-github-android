package co.binary.exploregithubandroid.feature.home.detail

import androidx.lifecycle.SavedStateHandle
import co.binary.exploregithubandroid.core.domain.user.GetGitHubUserDetailUseCase
import co.binary.exploregithubandroid.core.domain.user.GetGitHubUserReposUseCase
import co.binary.exploregithubandroid.core.testing.data.gitHubUserDetailTestData
import co.binary.exploregithubandroid.core.testing.repository.user.FakeGitHubUserRepository
import co.binary.exploregithubandroid.core.testing.util.MainDispatcherRule
import co.binary.exploregithubandroid.feature.home.navigation.UserDetailNavigationArgs
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class GitHubUserDetailViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: GitHubUserDetailViewModel

    private lateinit var repository: FakeGitHubUserRepository

    private lateinit var getGitHubUserDetailUseCase: GetGitHubUserDetailUseCase

    private lateinit var getGitHubUserReposUseCase: GetGitHubUserReposUseCase

    private val thisUser = gitHubUserDetailTestData.first()
    private val otherUser = gitHubUserDetailTestData.last()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        repository = FakeGitHubUserRepository()
        getGitHubUserDetailUseCase = GetGitHubUserDetailUseCase(repository, testDispatcher)
        getGitHubUserReposUseCase = GetGitHubUserReposUseCase(repository, testDispatcher)

        initViewModel()
    }

    @Test
    fun when_there_is_no_matched_data_then_it_should_be_error() = runTest {
        val actual = viewModel.uiState.value
        Truth.assertThat(actual).isEqualTo(GitHubUserDetailUiState.Error)
    }

    @Test
    fun when_there_is_matched_data_then_it_should_be_success() {
        repository.addUserDetail(thisUser)
        initViewModel()
        val actual = viewModel.uiState.value
        Truth.assertThat(actual).isEqualTo(GitHubUserDetailUiState.Success(thisUser, 1))
    }

    @Test
    fun when_there_is_error_in_loading_more_then_it_should_show_error() {
        repository.addUserDetail(thisUser)
        initViewModel()
        repository.clear()

        val prev = viewModel.uiState.value as GitHubUserDetailUiState.Success
        viewModel.loadMore()
        val actual = viewModel.uiState.value as GitHubUserDetailUiState.Success
        Truth.assertThat(actual.error).isNotNull()

        // Same as previous state
        Truth.assertThat(actual.user).isEqualTo(prev.user)
        Truth.assertThat(actual.page).isEqualTo(prev.page)
        Truth.assertThat(actual.loadingMore).isFalse()
        Truth.assertThat(actual.allLoaded).isFalse()
    }

    @Test
    fun when_there_is_data_to_load_more_then_it_should_show_new_data() {
        repository.addUserDetail(thisUser)
        initViewModel()
        val lastRepo = thisUser.repos.last()
        val newRepos = listOf(lastRepo.copy(id = lastRepo.id + 1))
        repository.addUserDetail(thisUser.copy(repos = newRepos))

        val prev = viewModel.uiState.value as GitHubUserDetailUiState.Success
        viewModel.loadMore()
        val actual = viewModel.uiState.value as GitHubUserDetailUiState.Success

        // Same as previous state with new repos
        Truth.assertThat(actual).isEqualTo(
            prev.copy(
                user = prev.user.copy(repos = prev.user.repos + newRepos),
                page = 2,
                loadingMore = false,
            )
        )
    }

    @Test
    fun when_there_is_no_more_data_then_it_should_stop_loading() {
        repository.addUserDetail(thisUser)
        initViewModel()
        repository.addUserDetail(thisUser.copy(repos = emptyList()))

        val prev = viewModel.uiState.value
        viewModel.loadMore()
        val actual = viewModel.uiState.value
        Truth.assertThat(actual).isEqualTo((prev as GitHubUserDetailUiState.Success).copy(allLoaded = true))
    }

    private fun initViewModel() {
        viewModel = GitHubUserDetailViewModel(
            getGitHubUserDetailUseCase = getGitHubUserDetailUseCase,
            getGitHubUserReposUseCase = getGitHubUserReposUseCase,
            savedStateHandle = SavedStateHandle(mapOf(UserDetailNavigationArgs.USERNAME to thisUser.username))
        )
    }
}
