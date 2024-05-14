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
import co.binary.exploregithubandroid.core.model.GitHubUser
import co.binary.exploregithubandroid.core.model.dummyUsers
import co.binary.exploregithubandroid.core.ui.DevicePreview
import co.binary.exploregithubandroid.core.ui.component.EndlessLazyColumn
import co.binary.exploregithubandroid.core.ui.theme.ExploreGitHubAndroidTheme
import co.binary.exploregithubandroid.feature.home.R
import coil.compose.AsyncImage

@Composable
internal fun SearchGitHubUserRoute(
    modifier: Modifier = Modifier,
    viewModel: SearchGitHubUserViewModel = hiltViewModel(),
    goToUserDetail: (username: String) -> Unit,
) {
    // Collect the UI state in a life cycle aware manner
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SearchGitHubUserScreen(
        modifier = modifier,
        uiState = uiState,
        onSearch = viewModel::search,
        onUserClick = goToUserDetail,
        loadMore = viewModel::loadMore,
    )
}

@Composable
private fun SearchGitHubUserScreen(
    modifier: Modifier = Modifier,
    uiState: SearchGitHubUsersUiState,
    onSearch: (searchText: String) -> Unit,
    onUserClick: (username: String) -> Unit,
    loadMore: () -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        // Remember the search text during configuration changes
        var searchText by rememberSaveable { mutableStateOf("") }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { Text("Search GitHub users") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search icon") },
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    IconButton(onClick = { searchText = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear search text")
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
                is SearchGitHubUsersUiState.Initial -> {
                    Text("Search and explore GitHub users", style = MaterialTheme.typography.bodyLarge)
                }

                is SearchGitHubUsersUiState.Empty -> {
                    Text("No users found", style = MaterialTheme.typography.bodyLarge)
                }

                is SearchGitHubUsersUiState.Error -> {
                    Text("Something went wrong. Please try again.", style = MaterialTheme.typography.bodyLarge)
                }

                is SearchGitHubUsersUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is SearchGitHubUsersUiState.Success -> {
                    UserList(
                        users = uiState.users,
                        shouldLoadMore = uiState.shouldLoadMore,
                        loadingMore = uiState.loadingMore,
                        onUserClick = onUserClick,
                        loadMore = loadMore
                    )
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
            // FIXME: Fix placeholder
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

private class SearchGitHubUserUiStateProvider : PreviewParameterProvider<SearchGitHubUsersUiState> {
    override val values: Sequence<SearchGitHubUsersUiState> = sequenceOf(
        SearchGitHubUsersUiState.Success(
            searchText = "",
            users = dummyUsers,
            shouldLoadMore = true,
            loadingMore = false
        ),
        SearchGitHubUsersUiState.Initial(),
        SearchGitHubUsersUiState.Empty(),
        SearchGitHubUsersUiState.Error(),
        SearchGitHubUsersUiState.Loading(),
    )
}

@DevicePreview
@Composable
private fun SearchGitHubUserScreenPreview(@PreviewParameter(SearchGitHubUserUiStateProvider::class) uiState: SearchGitHubUsersUiState) {
    ExploreGitHubAndroidTheme {
        SearchGitHubUserScreen(
            uiState = uiState,
            onSearch = {},
            onUserClick = {},
            loadMore = {},
        )
    }
}
