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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.binary.exploregithubandroid.core.model.user.GitHubUser
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
    val searchBarUiState by viewModel.searchBarUiState.collectAsStateWithLifecycle()

    SearchGitHubUserScreen(
        modifier = modifier,
        uiState = uiState,
        searchBarUiState = searchBarUiState,
        onToggleSearch = viewModel::toggleSearch,
        onSearchTextChange = viewModel::updateSearchText,
        onClearSearchText = viewModel::clearSearchText,
        onSearch = viewModel::search,
        onShowSnackbar = onShowSnackbar,
        onClearSearches = viewModel::clearRecentSearches,
        onUserClick = goToUserDetail,
        loadMore = viewModel::loadMore,
    )
}

@Composable
internal fun SearchGitHubUserScreen(
    modifier: Modifier = Modifier,
    uiState: SearchGitHubUserUiState,
    searchBarUiState: SearchBarUiState,
    onToggleSearch: () -> Unit,
    onSearchTextChange: (searchText: String) -> Unit,
    onSearch: (searchText: String) -> Unit,
    onClearSearchText: () -> Unit,
    onClearSearches: () -> Unit,
    onUserClick: (username: String) -> Unit,
    loadMore: () -> Unit,
    onShowSnackbar: suspend (String) -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        val searchBarPadding =
            if (searchBarUiState.isSearching) PaddingValues(0.dp) else PaddingValues(
                horizontal = 16.dp,
                vertical = 8.dp
            )

        UserSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(searchBarPadding),
            searchBarUiState = searchBarUiState,
            uiState = uiState,
            onToggleSearch = onToggleSearch,
            onSearchTextChange = onSearchTextChange,
            onClearSearchText = onClearSearchText,
            onSearch = onSearch,
            onClearSearches = onClearSearches
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
                        uiState.error.toErrorMessage(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                is SearchGitHubUserUiState.Loading -> {
                    val cd = stringResource(R.string.cd_loading)
                    CircularProgressIndicator(modifier = Modifier.semantics { contentDescription = cd })
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
@OptIn(ExperimentalMaterial3Api::class)
private fun UserSearchBar(
    modifier: Modifier = Modifier,
    searchBarUiState: SearchBarUiState,
    uiState: SearchGitHubUserUiState,
    onToggleSearch: () -> Unit,
    onSearchTextChange: (searchText: String) -> Unit,
    onClearSearchText: () -> Unit,
    onSearch: (searchText: String) -> Unit,
    onClearSearches: () -> Unit
) {
    SearchBar(
        modifier = modifier,
        query = if (searchBarUiState.isSearching) searchBarUiState.searchBarText else uiState.queryText,
        onQueryChange = { onSearchTextChange(it) },
        onSearch = {
            onSearch(it)
            onToggleSearch()
        },
        active = searchBarUiState.isSearching,
        onActiveChange = { onToggleSearch() },
        placeholder = { Text(stringResource(R.string.search_github_users_hint)) },
        leadingIcon = {
            if (searchBarUiState.isSearching) {
                IconButton(onClick = { onToggleSearch() }) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.cd_go_back),
                    )
                }
            } else {
                Icon(
                    Icons.Default.Search,
                    contentDescription = stringResource(R.string.search_github_users_hint)
                )
            }
        },
        trailingIcon = {
            if (searchBarUiState.isSearching) {
                if (searchBarUiState.searchBarText.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            onSearchTextChange("")
                        }
                    ) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = stringResource(R.string.cd_clear_search_text)
                        )
                    }
                }
            } else {
                if (uiState.queryText.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            onClearSearchText()
                        }
                    ) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = stringResource(R.string.cd_clear_search_text)
                        )
                    }
                }
            }
        },
    ) {
        RecentSearchList(
            modifier = Modifier.fillMaxSize(),
            searchBarUiState = searchBarUiState,
            onToggleSearch = onToggleSearch,
            onSearch = onSearch,
            onClearSearches = onClearSearches,
        )
    }
}

@Composable
private fun RecentSearchList(
    modifier: Modifier = Modifier,
    searchBarUiState: SearchBarUiState,
    onToggleSearch: () -> Unit,
    onSearch: (searchText: String) -> Unit,
    onClearSearches: () -> Unit,
) {
    LazyColumn(modifier = modifier) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.recent_searches_text),
                    style = MaterialTheme.typography.titleMedium
                )

                IconButton(onClick = onClearSearches, modifier = Modifier.padding(start = 16.dp)) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = stringResource(R.string.cd_clear_search_text)
                    )
                }
            }
        }
        items(
            items = searchBarUiState.recentSearches,
            key = {
                // Each string is unique and can serve as key
                it
            }
        ) { item ->
            Text(
                text = item,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSearch(item)
                        onToggleSearch()
                    }
                    .padding(16.dp)
            )
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

@DevicePreview
@Composable
private fun SearchGitHubUserScreenPreview(@PreviewParameter(SearchGitHubUserUiStateProvider::class) uiState: SearchGitHubUserUiState) {
    ExploreGitHubAndroidTheme {
        SearchGitHubUserScreen(
            uiState = uiState,
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
}

@DevicePreview
@Composable
private fun SearchGitHubUserScreenSearchBarPreview() {
    ExploreGitHubAndroidTheme {
        SearchGitHubUserScreen(
            uiState = SearchGitHubUserUiState.Initial(),
            searchBarUiState = SearchBarUiState(
                isSearching = true,
                searchBarText = "test",
                recentSearches = listOf("test1", "test2"),
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
}

/**
 * Map to user friendly error message. Move to ui module when other modules require.
 */
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
