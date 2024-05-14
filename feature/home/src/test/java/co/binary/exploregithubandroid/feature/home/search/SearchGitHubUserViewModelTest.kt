package co.binary.exploregithubandroid.feature.home.search

import co.binary.exploregithubandroid.core.domain.user.SearchGitHubUsersUseCase
import co.binary.exploregithubandroid.core.repository.user.GitHubUserRepository
import co.binary.exploregithubandroid.core.testing.data.gitHubUserTestData
import co.binary.exploregithubandroid.core.testing.util.MainDispatcherRule
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class SearchGitHubUserViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: SearchGitHubUserViewModel

    private lateinit var repository: GitHubUserRepository

    private val gitHubUsers = gitHubUserTestData
    private val thisUser = gitHubUsers.first()
    private val otherUser = gitHubUsers.last()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        repository = spyk()
        val searchGitHubUserUseCase = SearchGitHubUsersUseCase(repository, testDispatcher)
        viewModel = SearchGitHubUserViewModel(searchGitHubUserUseCase = searchGitHubUserUseCase)
    }

    @Test
    fun when_initial_then_it_should_show_initial_state() = runTest {
        val actual = viewModel.uiState.value
        Truth.assertThat(actual).isEqualTo(SearchGitHubUserUiState.Initial())
    }

    @Test
    fun when_search_with_empty_then_it_should_clear_data_and_should_not_fetch_data() {
        viewModel.search("")

        val actual = viewModel.uiState.value
        Truth.assertThat(actual).isEqualTo(SearchGitHubUserUiState.Initial())
        coVerify(exactly = 0) { repository.searchGitHubUsers(any(), any()) }
    }

    @Test
    fun when_search_with_whitespaces_then_it_should_clear_data_and_should_not_fetch_data() {
        viewModel.search("     ")

        val actual = viewModel.uiState.value
        Truth.assertThat(actual).isEqualTo(SearchGitHubUserUiState.Initial())
        coVerify(exactly = 0) { repository.searchGitHubUsers(any(), any()) }
    }

    @Test
    fun when_search_returns_error_then_it_should_show_error() {
        coEvery { repository.searchGitHubUsers(any(), any()) } returns Result.failure(Exception("Error"))
        viewModel.search(thisUser.username)

        val actual = viewModel.uiState.value
        Truth.assertThat(actual).isEqualTo(SearchGitHubUserUiState.Error(thisUser.username))
    }

    @Test
    fun when_search_returns_empty_data_then_it_should_show_empty() {
        coEvery { repository.searchGitHubUsers(any(), any()) } returns Result.success(emptyList())

        viewModel.search(thisUser.username)

        val actual = viewModel.uiState.value
        Truth.assertThat(actual).isEqualTo(SearchGitHubUserUiState.Empty(thisUser.username))
    }

    @Test
    fun when_search_returns_data_then_it_should_show_data() {
        coEvery { repository.searchGitHubUsers(any(), any()) } returns Result.success(listOf(thisUser))

        viewModel.search(thisUser.username)

        val actual = viewModel.uiState.value
        Truth.assertThat(actual).isEqualTo(
            SearchGitHubUserUiState.Success(
                searchText = thisUser.username,
                users = listOf(thisUser),
                shouldLoadMore = true,
                loadingMore = false,
                error = null
            )
        )
    }

    @Test
    fun when_loadMore_returns_error_then_it_should_show_error() {
        coEvery { repository.searchGitHubUsers(any(), 1) } returns Result.success(listOf(thisUser))
        coEvery { repository.searchGitHubUsers(any(), 2) } returns Result.failure(Exception("Error"))

        viewModel.search(thisUser.username)
        val prev = viewModel.uiState.value as SearchGitHubUserUiState.Success

        viewModel.loadMore()
        val actual = viewModel.uiState.value as SearchGitHubUserUiState.Success
        Truth.assertThat(actual.error).isNotNull()

        // Same as previous state
        Truth.assertThat(actual.users).isEqualTo(prev.users)
        Truth.assertThat(actual.loadingMore).isFalse()
        Truth.assertThat(actual.shouldLoadMore).isTrue()
    }

    @Test
    fun when_loadMore_returns_data_then_it_should_show_new_data() {
        coEvery { repository.searchGitHubUsers(any(), 1) } returns Result.success(listOf(thisUser))
        coEvery { repository.searchGitHubUsers(any(), 2) } returns Result.success(listOf(otherUser))

        viewModel.search(thisUser.username)
        val prev = viewModel.uiState.value as SearchGitHubUserUiState.Success

        viewModel.loadMore()
        val actual = viewModel.uiState.value as SearchGitHubUserUiState.Success
        Truth.assertThat(actual.users).isEqualTo(prev.users + listOf(otherUser))
        Truth.assertThat(actual.loadingMore).isFalse()
        Truth.assertThat(actual.shouldLoadMore).isTrue()
        Truth.assertThat(actual.error).isNull()
    }

    @Test
    fun when_loadMore_returns_no_more_data_then_it_should_stop_loading() {
        coEvery { repository.searchGitHubUsers(any(), 1) } returns Result.success(listOf(thisUser))
        coEvery { repository.searchGitHubUsers(any(), 2) } returns Result.success(emptyList())

        viewModel.search(thisUser.username)
        val prev = viewModel.uiState.value as SearchGitHubUserUiState.Success

        viewModel.loadMore()
        val actual = viewModel.uiState.value
        Truth.assertThat(actual).isEqualTo(prev.copy(shouldLoadMore = false))
    }
}

