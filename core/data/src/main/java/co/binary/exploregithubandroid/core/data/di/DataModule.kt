package co.binary.exploregithubandroid.core.data.di

import co.binary.exploregithubandroid.core.data.user.repository.GitHubUserRepositoryImpl
import co.binary.exploregithubandroid.core.data.user.repository.RecentSearchRepositoryImpl
import co.binary.exploregithubandroid.core.repository.user.GitHubUserRepository
import co.binary.exploregithubandroid.core.repository.user.RecentSearchRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindsGitHubUserRepository(impl: GitHubUserRepositoryImpl): GitHubUserRepository

    @Binds
    @Singleton
    abstract fun bindsRecentSearchRepository(impl: RecentSearchRepositoryImpl): RecentSearchRepository
}
