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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.binary.exploregithubandroid.core.designsystem.DevicePreview
import co.binary.exploregithubandroid.core.designsystem.theme.ExploreGitHubAndroidTheme
import co.binary.exploregithubandroid.core.model.GitHubUser
import co.binary.exploregithubandroid.core.model.dummyUser
import co.binary.exploregithubandroid.core.model.dummyUsers
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

    val searchText by viewModel.searchText.collectAsStateWithLifecycle()

    SearchGitHubUserScreen(
        modifier = modifier,
        uiState = uiState,
        searchText = searchText,
        onSearchTextChange = viewModel::updateSearchText,
        onSearch = viewModel::search,
        onClearSearchText = viewModel::clearSearchText,
        onUserClick = goToUserDetail,
    )
}

@Composable
private fun SearchGitHubUserScreen(
    modifier: Modifier = Modifier,
    searchText: String,
    uiState: SearchGitHubUsersUiState,
    onSearchTextChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onClearSearchText: () -> Unit,
    onUserClick: (username: String) -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            value = searchText,
            onValueChange = onSearchTextChange,
            placeholder = { Text("Search GitHub users") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search icon") },
            trailingIcon = {
                IconButton(onClick = onClearSearchText) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear search text")
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
                SearchGitHubUsersUiState.Initial -> {
                    Text("Search and explore GitHub users", style = MaterialTheme.typography.bodyLarge)
                }

                SearchGitHubUsersUiState.Empty -> {
                    Text("No users found", style = MaterialTheme.typography.bodyLarge)
                }

                SearchGitHubUsersUiState.Error -> {
                    Text("Something went wrong. Please try again.", style = MaterialTheme.typography.bodyLarge)
                }

                SearchGitHubUsersUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is SearchGitHubUsersUiState.Success -> {
                    UserList(users = uiState.users, onUserClick = onUserClick)
                }
            }
        }
    }
}

@Composable
private fun UserList(
    modifier: Modifier = Modifier,
    users: List<GitHubUser>,
    onUserClick: (username: String) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        items(
            users,
            key = {
                // Use the ID as the unique and stable key
                it.id
            },
        ) { user ->
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
    }
}

@Preview
@Composable
private fun UserListPreview() {
    ExploreGitHubAndroidTheme {
        UserList(
            users = listOf(dummyUser),
            onUserClick = {},
        )
    }
}

private class SearchGitHubUserUiStateProvider : PreviewParameterProvider<SearchGitHubUsersUiState> {
    override val values: Sequence<SearchGitHubUsersUiState> = sequenceOf(
        SearchGitHubUsersUiState.Success(users = dummyUsers),
        SearchGitHubUsersUiState.Initial,
        SearchGitHubUsersUiState.Empty,
        SearchGitHubUsersUiState.Error,
        SearchGitHubUsersUiState.Loading,
    )
}

@DevicePreview
@Composable
private fun SearchGitHubUserScreenPreview(@PreviewParameter(SearchGitHubUserUiStateProvider::class) uiState: SearchGitHubUsersUiState) {
    ExploreGitHubAndroidTheme {
        SearchGitHubUserScreen(
            uiState = uiState,
            searchText = "",
            onSearchTextChange = {},
            onSearch = {},
            onClearSearchText = {},
            onUserClick = {},
        )
    }
}
