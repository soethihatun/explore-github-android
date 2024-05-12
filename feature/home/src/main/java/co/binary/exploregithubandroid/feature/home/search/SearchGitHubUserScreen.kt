package co.binary.exploregithubandroid.feature.home.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.binary.exploregithubandroid.core.designsystem.theme.ExploreGitHubAndroidTheme
import co.binary.exploregithubandroid.core.model.GitHubUser
import coil.compose.AsyncImage

@Composable
internal fun SearchGitHubUserRoute(
    modifier: Modifier = Modifier,
    viewModel: SearchGitHubUserViewModel = hiltViewModel(),
) {
    // Collect the UI state in a life cycle aware manner
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SearchGitHubUserScreen(
        modifier = modifier,
        uiState = uiState,
        onUserClick = {},
    )
}

@Composable
private fun SearchGitHubUserScreen(
    modifier: Modifier = Modifier,
    uiState: SearchUserUiState,
    onUserClick: (login: String) -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        // TODO: Add search box
        Text("Search the user")

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                SearchUserUiState.Initial -> {
                    Text("Search and explore GitHub users", style = MaterialTheme.typography.bodyLarge)
                }

                SearchUserUiState.Empty -> {
                    Text("No users found", style = MaterialTheme.typography.bodyLarge)
                }

                SearchUserUiState.Error -> {
                    Text("Something went wrong. Please try again.", style = MaterialTheme.typography.bodyLarge)
                }

                SearchUserUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is SearchUserUiState.Success -> {
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
    onUserClick: (login: String) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(
            users,
            key = {
                // Use the ID as the unique and stable key
                it.id
            },
        ) { user ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onUserClick(user.login) },
            ) {
                AsyncImage(
                    model = user.avatarUrl,
                    contentDescription = "avatar image",
                    // FIXME: Fix placeholder
                    placeholder = rememberVectorPainter(image = Icons.Default.Person),
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(user.name, style = MaterialTheme.typography.bodyLarge)

                    Text(user.login, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

private val dummyUser = GitHubUser(id = 1, login = "johndoe", avatarUrl = "avatarUrl", name = "John Doe")

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

private class SearchUserUiStateProvider : PreviewParameterProvider<SearchUserUiState> {
    override val values: Sequence<SearchUserUiState> = sequenceOf(
        SearchUserUiState.Initial,
        SearchUserUiState.Empty,
        SearchUserUiState.Error,
        SearchUserUiState.Loading,
        SearchUserUiState.Success(users = listOf(dummyUser))
    )
}

@Preview
@Composable
private fun SearchGitHubUserScreenPreview(@PreviewParameter(SearchUserUiStateProvider::class) uiState: SearchUserUiState) {
    ExploreGitHubAndroidTheme {
        SearchGitHubUserScreen(
            uiState = uiState,
            onUserClick = {},
        )
    }
}
