package co.binary.exploregithubandroid.feature.home.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.binary.exploregithubandroid.core.designsystem.DevicePreview
import co.binary.exploregithubandroid.core.designsystem.theme.ExploreGitHubAndroidTheme
import co.binary.exploregithubandroid.core.model.GitHubRepo
import co.binary.exploregithubandroid.core.model.GitHubUserDetail
import co.binary.exploregithubandroid.core.model.dummyRepos
import co.binary.exploregithubandroid.core.model.dummyUserDetail
import coil.compose.AsyncImage

@Composable
internal fun GitHubUserDetailRoute(
    modifier: Modifier = Modifier,
    viewModel: GitHubUserDetailViewModel = hiltViewModel(),
) {
    // Collect the UI state in a life cycle aware manner
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GitHubUserDetailScreen(
        modifier = modifier,
        uiState = uiState,
    )
}

@Composable
private fun GitHubUserDetailScreen(modifier: Modifier = Modifier, uiState: GitHubUserDetailUiState) {
    Column(modifier = modifier.fillMaxSize()) {
        when (uiState) {
            GitHubUserDetailUiState.Error -> {
                Text("Something went wrong. Please try again.", style = MaterialTheme.typography.bodyLarge)
            }

            GitHubUserDetailUiState.Loading -> {
                CircularProgressIndicator()
            }

            is GitHubUserDetailUiState.Success -> {
                GitHubUserDetailContent(user = uiState.user)
            }
        }
    }
}

@Composable
private fun GitHubUserDetailContent(modifier: Modifier = Modifier, user: GitHubUserDetail) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    model = user.avatarUrl,
                    contentDescription = "avatar image",
                    // FIXME: Fix placeholder
                    placeholder = rememberVectorPainter(image = Icons.Default.Person),
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(user.name, style = MaterialTheme.typography.titleLarge)

                    Text(user.login, style = MaterialTheme.typography.titleMedium)
                }
            }

            Row(modifier = Modifier.height(IntrinsicSize.Min).padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, contentDescription = "followers & following users")

                Spacer(modifier = Modifier.width(8.dp))

                Text(user.followers.toString(), style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.width(2.dp))

                Text("followers", style = MaterialTheme.typography.bodyLarge)

                VerticalDivider(
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 8.dp)
                )

                Text(user.following.toString(), style = MaterialTheme.typography.bodyLarge)

                Spacer(modifier = Modifier.width(2.dp))

                Text("following", style = MaterialTheme.typography.bodyLarge)
            }
        }

        UserRepositoryList(repos = user.repos)
    }
}

@Composable
private fun UserRepositoryList(modifier: Modifier = Modifier, repos: List<GitHubRepo>) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(items = repos, key = { _, repo -> repo.id }) { index, repo ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(repo.name, style = MaterialTheme.typography.bodyLarge)
                Text(
                    repo.description,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Row(modifier = Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Star, contentDescription = "followers & following users")
                        Text(repo.stargazersCount.toString(), style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color.Yellow, CircleShape)
                        )
                        Text(repo.primaryLanguage, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            if (index < repos.lastIndex) {
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Preview
@Composable
private fun UserRepositoryListPreview() {
    ExploreGitHubAndroidTheme {
        UserRepositoryList(repos = dummyRepos)
    }
}

private class GitHubUserDetailUiStateProvider : PreviewParameterProvider<GitHubUserDetailUiState> {
    override val values: Sequence<GitHubUserDetailUiState> = sequenceOf(
        GitHubUserDetailUiState.Success(dummyUserDetail),
        GitHubUserDetailUiState.Loading,
        GitHubUserDetailUiState.Error,
    )
}

@DevicePreview
@Composable
private fun GitHubUserDetailScreenPreview(@PreviewParameter(GitHubUserDetailUiStateProvider::class) uiState: GitHubUserDetailUiState) {
    ExploreGitHubAndroidTheme {
        GitHubUserDetailScreen(uiState = uiState)
    }
}
