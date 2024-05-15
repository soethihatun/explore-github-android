package co.binary.exploregithubandroid.feature.home.search

import co.binary.exploregithubandroid.core.domain.user.ClearRecentSearchUseCase
import co.binary.exploregithubandroid.core.domain.user.GetRecentSearchStreamUseCase
import co.binary.exploregithubandroid.core.domain.user.InsertOrReplaceRecentSearchUseCase
import co.binary.exploregithubandroid.core.domain.user.SearchGitHubUsersUseCase
import co.binary.exploregithubandroid.core.repository.user.GitHubUserRepository
import co.binary.exploregithubandroid.core.repository.user.RecentSearchRepository
import co.binary.exploregithubandroid.core.testing.data.gitHubUserTestData
import co.binary.exploregithubandroid.core.testing.util.MainDispatcherRule
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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

    private lateinit var userRepository: GitHubUserRepository
    private lateinit var recentRepository: RecentSearchRepository

    private val gitHubUsers = gitHubUserTestData
    private val thisUser = gitHubUsers.first()
    private val otherUser = gitHubUsers.last()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        userRepository = spyk()
        recentRepository = mockk()

        coEvery { recentRepository.getRecentSearchStream(any()) } returns flowOf(emptyList())
        coEvery { recentRepository.insertOrReplaceRecentSearch(any()) } returns Unit

        val searchGitHubUserUseCase = SearchGitHubUsersUseCase(userRepository, testDispatcher)
        val recentSearchStreamUseCase = GetRecentSearchStreamUseCase(recentRepository)
        val insertOrReplaceRecentSearchUseCase = InsertOrReplaceRecentSearchUseCase(recentRepository, testDispatcher)
        val clearRecentSearchUseCase = ClearRecentSearchUseCase(recentRepository, testDispatcher)
        viewModel = SearchGitHubUserViewModel(
            searchGitHubUserUseCase = searchGitHubUserUseCase,
            recentSearchStreamUseCase = recentSearchStreamUseCase,
            insertOrReplaceRecentSearchUseCase = insertOrReplaceRecentSearchUseCase,
            clearRecentSearchUseCase = clearRecentSearchUseCase,
        )
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
        coVerify(exactly = 0) { userRepository.searchGitHubUsers(any(), any()) }
    }

    @Test
    fun when_search_with_whitespaces_then_it_should_clear_data_and_should_not_fetch_data() {
        viewModel.search("     ")

        val actual = viewModel.uiState.value
        Truth.assertThat(actual).isEqualTo(SearchGitHubUserUiState.Initial())
        coVerify(exactly = 0) { userRepository.searchGitHubUsers(any(), any()) }
    }

    @Test
    fun when_search_returns_error_then_it_should_show_error() {
        val error = Exception("Error")
        coEvery { userRepository.searchGitHubUsers(any(), any()) } returns Result.failure(error)
        viewModel.search(thisUser.username)

        val actual = viewModel.uiState.value
        Truth.assertThat(actual).isEqualTo(SearchGitHubUserUiState.Error(thisUser.username, error))
    }

    @Test
    fun when_search_returns_empty_data_then_it_should_show_empty() {
        coEvery { userRepository.searchGitHubUsers(any(), any()) } returns Result.success(emptyList())

        viewModel.search(thisUser.username)

        val actual = viewModel.uiState.value
        Truth.assertThat(actual).isEqualTo(SearchGitHubUserUiState.Empty(thisUser.username))
    }

    @Test
    fun when_search_returns_data_then_it_should_show_data() {
        coEvery { userRepository.searchGitHubUsers(any(), any()) } returns Result.success(listOf(thisUser))

        viewModel.search(thisUser.username)

        val actual = viewModel.uiState.value
        Truth.assertThat(actual).isEqualTo(
            SearchGitHubUserUiState.Success(
                queryText = thisUser.username,
                users = listOf(thisUser),
                shouldLoadMore = true,
                loadingMore = false,
                error = null
            )
        )
    }

    @Test
    fun when_loadMore_returns_error_then_it_should_show_error() {
        coEvery { userRepository.searchGitHubUsers(any(), 1) } returns Result.success(listOf(thisUser))
        coEvery { userRepository.searchGitHubUsers(any(), 2) } returns Result.failure(Exception("Error"))

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
        coEvery { userRepository.searchGitHubUsers(any(), 1) } returns Result.success(listOf(thisUser))
        coEvery { userRepository.searchGitHubUsers(any(), 2) } returns Result.success(listOf(otherUser))

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
        coEvery { userRepository.searchGitHubUsers(any(), 1) } returns Result.success(listOf(thisUser))
        coEvery { userRepository.searchGitHubUsers(any(), 2) } returns Result.success(emptyList())

        viewModel.search(thisUser.username)
        val prev = viewModel.uiState.value as SearchGitHubUserUiState.Success

        viewModel.loadMore()
        val actual = viewModel.uiState.value
        Truth.assertThat(actual).isEqualTo(prev.copy(shouldLoadMore = false))
    }
}

