package co.binary.exploregithubandroid.core.designsystem

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreGitHubTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    navigationIcon: ImageVector? = null,
    navigationIconColor: Color = MaterialTheme.colorScheme.onSurface,
    actionIcon: ImageVector? = null,
    actionIconColor: Color = MaterialTheme.colorScheme.onSurface,
    onNavigationClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
) {
    TopAppBar(
        modifier = modifier,
        title = { Text(title) },
        navigationIcon = {
            navigationIcon?.let { icon ->
                IconButton(onClick = onNavigationClick) {
                    Icon(icon, contentDescription = null, tint = navigationIconColor)
                }
            }
        },
        actions = {
            actionIcon?.let { icon ->
                IconButton(onClick = onActionClick) {
                    Icon(icon, contentDescription = null, tint = actionIconColor)
                }
            }
        },
        colors = colors,
    )
}