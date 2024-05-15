package co.binary.exploregithubandroid.feature.home.search

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToNode
import co.binary.exploregithubandroid.core.testing.data.gitHubUserTestData
import co.binary.exploregithubandroid.feature.home.R
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.UnknownHostException

class SearchGitHubUserScreenTest {
    /**
     * Create rule with the base ComponentActivity, which is the base of the activities
     */
    @get:Rule
    internal val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var loading: String

    private val testData = gitHubUserTestData

    @Before
    fun setup() {
        composeTestRule.activity.apply {
            loading = getString(R.string.cd_loading)
        }
    }

    @Test
    fun whenScreenIsInitialized_showIntro() {
        composeTestRule.apply {
            setContent {
                SearchGitHubUserScreen(
                    uiState = SearchGitHubUserUiState.Initial(),
                    searchBarUiState = SearchBarUiState(),
                    onSearch = {},
                    onClearSearches = {},
                    onUserClick = {},
                    loadMore = {},
                    onShowSnackbar = {},
                    onClearSearchText = {},
                    onToggleSearch = {},
                    onSearchTextChange = {},
                )
            }

            val intro = activity.getString(R.string.search_github_user_intro_message)
            onNodeWithText(intro).assertExists()
        }
    }

    @Test
    fun whenDataIsLoading_showLoading() {
        composeTestRule.apply {
            setContent {
                SearchGitHubUserScreen(
                    uiState = SearchGitHubUserUiState.Loading(),
                    searchBarUiState = SearchBarUiState(),
                    onSearch = {},
                    onClearSearches = {},
                    onUserClick = {},
                    loadMore = {},
                    onShowSnackbar = {},
                    onClearSearchText = {},
                    onToggleSearch = {},
                    onSearchTextChange = {},
                )
            }

            onNodeWithContentDescription(loading).assertExists()
        }
    }

    @Test
    fun whenDataLoadsError_showError() {
        composeTestRule.apply {
            val exception = UnknownHostException()
            var errorMessage = ""
            setContent {
                SearchGitHubUserScreen(
                    uiState = SearchGitHubUserUiState.Error(error = exception),
                    searchBarUiState = SearchBarUiState(),
                    onSearch = {},
                    onClearSearches = {},
                    onUserClick = {},
                    loadMore = {},
                    onShowSnackbar = {},
                    onClearSearchText = {},
                    onToggleSearch = {},
                    onSearchTextChange = {},
                )
                errorMessage = exception.toErrorMessage()
            }

            onNodeWithText(errorMessage).assertExists()
        }
    }

    @Test
    fun whenDataLoadsEmpty_showEmpty() {
        composeTestRule.apply {
            setContent {
                SearchGitHubUserScreen(
                    uiState = SearchGitHubUserUiState.Empty(queryText = testData.first().username),
                    searchBarUiState = SearchBarUiState(),
                    onSearch = {},
                    onClearSearches = {},
                    onUserClick = {},
                    loadMore = {},
                    onShowSnackbar = {},
                    onClearSearchText = {},
                    onToggleSearch = {},
                    onSearchTextChange = {},
                )
            }

            val error = activity.getString(R.string.no_user_found_error_message)

            onNodeWithText(error).assertExists()
        }
    }

    @Test
    fun whenDataLoadsData_showData() {
        composeTestRule.apply {
            setContent {
                SearchGitHubUserScreen(
                    uiState = SearchGitHubUserUiState.Success(
                        queryText = testData.first().username,
                        users = testData,
                        shouldLoadMore = true,
                    ),
                    searchBarUiState = SearchBarUiState(),
                    onSearch = {},
                    onClearSearches = {},
                    onUserClick = {},
                    loadMore = {},
                    onShowSnackbar = {},
                    onClearSearchText = {},
                    onToggleSearch = {},
                    onSearchTextChange = {},
                )
            }

            // Scroll to first item if available
            onAllNodes(hasScrollToNodeAction())
                .onFirst()
                .performScrollToNode(hasText(testData.first().username))
        }
    }

    @Test
    fun whenLoadMoreHasError_showDataWithError() {
        composeTestRule.apply {
            // Network error
            val exception = UnknownHostException()
            var errorMessage = ""
            var actual = ""
            setContent {
                SearchGitHubUserScreen(
                    uiState = SearchGitHubUserUiState.Success(
                        queryText = testData.first().username,
                        users = testData,
                        shouldLoadMore = true,
                        error = exception,
                    ),
                    searchBarUiState = SearchBarUiState(),
                    onSearch = {},
                    onClearSearches = {},
                    onUserClick = {},
                    loadMore = {},
                    onShowSnackbar = { actual = it },
                    onClearSearchText = {},
                    onToggleSearch = {},
                    onSearchTextChange = {},
                )
                errorMessage = exception.toErrorMessage()
            }

            Truth.assertThat(actual).isEqualTo(errorMessage)

            // Scroll to first item if available (there is still data)
            onAllNodes(hasScrollToNodeAction())
                .onFirst()
                .performScrollToNode(hasText(testData.first().username))
        }
    }

    @Test
    fun whenIsSearching_showSearchBar() {
        composeTestRule.apply {
            val searchBarText = "searchBarText"
            val recentSearches = listOf("Item 1", "Item 2")
            setContent {
                SearchGitHubUserScreen(
                    uiState = SearchGitHubUserUiState.Initial(),
                    searchBarUiState = SearchBarUiState(
                        isSearching = true,
                        searchBarText = "searchBarText",
                        recentSearches = recentSearches,
                    ),
                    onSearch = {},
                    onClearSearches = {},
                    onUserClick = {},
                    loadMore = {},
                    onShowSnackbar = {},
                    onClearSearchText = {},
                    onToggleSearch = {},
                    onSearchTextChange = {},
                )
            }

            onNodeWithText(searchBarText).assertExists()

            // Recent searches
            val recentSearchesText = activity.getString(R.string.recent_searches_text)
            onAllNodes(hasScrollToNodeAction())
                .onFirst()
                .performScrollToNode(hasText(recentSearchesText))

            // Scroll to first item if available
            onAllNodes(hasScrollToNodeAction())
                .onLast()
                .performScrollToNode(hasText(recentSearches.last()))
        }
    }
}