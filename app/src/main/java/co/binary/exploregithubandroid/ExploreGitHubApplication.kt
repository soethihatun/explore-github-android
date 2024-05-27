package co.binary.exploregithubandroid

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ExploreGitHubApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}
