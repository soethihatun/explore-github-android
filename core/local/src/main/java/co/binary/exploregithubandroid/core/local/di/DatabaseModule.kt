package co.binary.exploregithubandroid.core.local.di

import android.content.Context
import co.binary.exploregithubandroid.core.local.database.room.ExploreGitHubDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesExploreGitHubDatabase(@ApplicationContext context: Context) =
        ExploreGitHubDatabase.buildDatabase(context)

    @Provides
    @Singleton
    fun providesRecentSearchDao(database: ExploreGitHubDatabase) = database.recentSearchDao()
}
