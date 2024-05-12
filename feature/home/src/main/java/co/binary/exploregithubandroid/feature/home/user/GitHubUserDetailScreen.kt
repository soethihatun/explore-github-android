package co.binary.exploregithubandroid.feature.home.user

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import co.binary.exploregithubandroid.core.model.GitHubRepository
import co.binary.exploregithubandroid.core.model.GitHubUserDetail
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
    Column(modifier = modifier.fillMaxSize()) {
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
                    .size(100.dp)
                    .clip(CircleShape),
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(user.name, style = MaterialTheme.typography.titleLarge)

                Text(user.login, style = MaterialTheme.typography.titleMedium)
            }
        }

        Row(modifier = Modifier.height(IntrinsicSize.Min), verticalAlignment = Alignment.CenterVertically) {
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
}

@Composable
private fun UserRepositoryList(modifier: Modifier = Modifier, repos: List<GitHubRepository>) {
    // TODO:
}

private class GitHubUserDetailUiStateProvider : PreviewParameterProvider<GitHubUserDetailUiState> {
    override val values: Sequence<GitHubUserDetailUiState> = sequenceOf(
        GitHubUserDetailUiState.Success(dummyUserDetail),
        GitHubUserDetailUiState.Loading,
        GitHubUserDetailUiState.Error,
    )
}

@Preview
@Composable
private fun GitHubUserDetailScreenPreview(@PreviewParameter(GitHubUserDetailUiStateProvider::class) uiState: GitHubUserDetailUiState) {
    ExploreGitHubAndroidTheme {
        GitHubUserDetailScreen(uiState = uiState)
    }
}
