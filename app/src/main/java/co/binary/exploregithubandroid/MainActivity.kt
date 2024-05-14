package co.binary.exploregithubandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import co.binary.exploregithubandroid.core.designsystem.theme.ExploreGitHubAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExploreGitHubAndroidTheme {
                ExploreGitHubApp()
            }
        }
    }
}
