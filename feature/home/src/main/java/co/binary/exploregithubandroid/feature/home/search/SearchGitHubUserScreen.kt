package co.binary.exploregithubandroid.feature.home.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.binary.exploregithubandroid.core.model.user.GitHubUser
import co.binary.exploregithubandroid.core.model.user.dummyUsers
import co.binary.exploregithubandroid.core.ui.DevicePreview
import co.binary.exploregithubandroid.core.ui.component.EndlessLazyColumn
import co.binary.exploregithubandroid.core.ui.theme.ExploreGitHubAndroidTheme
import co.binary.exploregithubandroid.feature.home.R
import coil.compose.AsyncImage
import java.net.UnknownHostException

@Composable
internal fun SearchGitHubUserRoute(
    modifier: Modifier = Modifier,
    viewModel: SearchGitHubUserViewModel = hiltViewModel(),
    goToUserDetail: (username: String) -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
) {
    // Collect the UI state in a life cycle aware manner
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SearchGitHubUserScreen(
        modifier = modifier,
        uiState = uiState,
        onSearch = viewModel::search,
        onUserClick = goToUserDetail,
        loadMore = viewModel::loadMore,
        onShowSnackbar = onShowSnackbar,
    )
}

@Composable
private fun SearchGitHubUserScreen(
    modifier: Modifier = Modifier,
    uiState: SearchGitHubUserUiState,
    onSearch: (searchText: String) -> Unit,
    onUserClick: (username: String) -> Unit,
    loadMore: () -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        // Remember the search text during configuration changes
        var searchText by rememberSaveable { mutableStateOf("") }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { Text(stringResource(R.string.search_github_users_hint)) },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = stringResource(R.string.search_github_users_hint)
                )
            },
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    IconButton(onClick = { searchText = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = stringResource(R.string.cd_clear_search_text))
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    onSearch(searchText)
                }
            ),
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            when (uiState) {
                is SearchGitHubUserUiState.Initial -> {
                    Text(
                        stringResource(R.string.search_github_user_intro_message),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                is SearchGitHubUserUiState.Empty -> {
                    Text(
                        stringResource(R.string.no_user_found_error_message),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                is SearchGitHubUserUiState.Error -> {
                    Text(
                        stringResource(id = R.string.general_error_message),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                is SearchGitHubUserUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is SearchGitHubUserUiState.Success -> {
                    UserList(
                        users = uiState.users,
                        shouldLoadMore = uiState.shouldLoadMore,
                        loadingMore = uiState.loadingMore,
                        onUserClick = onUserClick,
                        loadMore = loadMore
                    )

                    uiState.error?.let {
                        val errorMessage = it.toErrorMessage()
                        LaunchedEffect(errorMessage) {
                            onShowSnackbar(errorMessage)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun UserList(
    modifier: Modifier = Modifier,
    shouldLoadMore: Boolean,
    loadingMore: Boolean,
    users: List<GitHubUser>,
    onUserClick: (username: String) -> Unit,
    loadMore: () -> Unit,
) {
    EndlessLazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
        enabled = shouldLoadMore,
        items = users,
        itemKey = { user ->
            // Use the ID as the unique and stable key
            user.id
        },
        itemContent = { user ->
            UserItem(onUserClick = onUserClick, user = user)
        },
        loading = loadingMore,
        loadingItem = {
            LoadingItem()
        },
        loadMore = loadMore,
    )
}

@Composable
private fun UserItem(
    onUserClick: (username: String) -> Unit,
    user: GitHubUser
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onUserClick(user.username) }
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        AsyncImage(
            model = user.avatarUrl,
            contentDescription = stringResource(id = R.string.cd_github_user_avatar_image),
            placeholder = rememberVectorPainter(image = Icons.Default.Person),
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape),
        )

        Text(user.username, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
internal fun LoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

private class SearchGitHubUserUiStateProvider : PreviewParameterProvider<SearchGitHubUserUiState> {
    override val values: Sequence<SearchGitHubUserUiState> = sequenceOf(
        SearchGitHubUserUiState.Success(
            searchText = "",
            users = dummyUsers,
            shouldLoadMore = true,
            loadingMore = false
        ),
        SearchGitHubUserUiState.Initial(),
        SearchGitHubUserUiState.Empty(),
        SearchGitHubUserUiState.Error(),
        SearchGitHubUserUiState.Loading(),
    )
}

@DevicePreview
@Composable
private fun SearchGitHubUserScreenPreview(@PreviewParameter(SearchGitHubUserUiStateProvider::class) uiState: SearchGitHubUserUiState) {
    ExploreGitHubAndroidTheme {
        SearchGitHubUserScreen(
            uiState = uiState,
            onSearch = {},
            onUserClick = {},
            loadMore = {},
            onShowSnackbar = {},
        )
    }
}

@Composable
internal fun Throwable.toErrorMessage(): String {
    return when (this) {
        is UnknownHostException -> {
            stringResource(id = R.string.network_error_message)
        }

        else -> {
            localizedMessage ?: stringResource(id = R.string.general_error_message)
        }
    }
}
