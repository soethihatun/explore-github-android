package co.binary.exploregithubandroid.feature.home.detail

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import co.binary.exploregithubandroid.core.testing.data.gitHubUserDetailTestData
import co.binary.exploregithubandroid.feature.home.R
import co.binary.exploregithubandroid.feature.home.search.toErrorMessage
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.net.UnknownHostException

class GitHubUserDetailScreenTest {
    /**
     * Create rule with the base ComponentActivity, which is the base of the activities
     */
    @get:Rule
    internal val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var loading: String
    private lateinit var generalError: String

    private val userDetail = gitHubUserDetailTestData.first()

    @Before
    fun setup() {
        composeTestRule.activity.apply {
            loading = getString(R.string.cd_loading)
            generalError = getString(R.string.general_error_message)
        }
    }

    @Test
    fun whenScreenIsLoading_showLoading() {
        composeTestRule.apply {
            setContent {
                GitHubUserDetailScreen(
                    uiState = GitHubUserDetailUiState.Loading,
                    onBackClick = {},
                    loadMore = {},
                    onShowSnackbar = {},
                )
            }

            onNodeWithContentDescription(loading).assertExists()
        }
    }

    @Test
    fun whenDataFailsToLoad_showError() {
        composeTestRule.apply {
            setContent {
                GitHubUserDetailScreen(
                    uiState = GitHubUserDetailUiState.Error,
                    onBackClick = {},
                    loadMore = {},
                    onShowSnackbar = {},
                )
            }

            onNodeWithText(generalError).assertExists()
        }
    }

    @Test
    fun whenDataLoads_showData() {
        composeTestRule.apply {
            setContent {
                GitHubUserDetailScreen(
                    uiState = GitHubUserDetailUiState.Success(
                        user = userDetail,
                        page = 1,
                    ),
                    onBackClick = {},
                    loadMore = {},
                    onShowSnackbar = {},
                )
            }

            onNodeWithText(userDetail.username).assertExists()
            userDetail.name?.let {
                onNodeWithText(it).assertExists()
            }
        }
    }

    @Test
    fun whenLoadMoreHasError_showDataWithError() {
        composeTestRule.apply {
            // Network error
            val exception = UnknownHostException()
            var actual = "actual"
            var errorMessage = "error"
            setContent {
                GitHubUserDetailScreen(
                    uiState = GitHubUserDetailUiState.Success(
                        user = userDetail,
                        page = 1,
                        error = exception,
                    ),
                    onBackClick = {},
                    loadMore = {},
                    onShowSnackbar = { message -> actual = message },
                )
                errorMessage = exception.toErrorMessage()
            }
            Truth.assertThat(actual).isEqualTo(errorMessage)
            onNodeWithText(userDetail.username).assertExists()
        }
    }
}
