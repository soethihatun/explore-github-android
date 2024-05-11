package co.binary.exploregithubandroid.core.data.di

import co.binary.exploregithubandroid.core.data.GitHubUserRepositoryImpl
import co.binary.exploregithubandroid.core.repository.GitHubUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {
    @Binds
    abstract fun bindsGitHubUserRepository(
        impl: GitHubUserRepositoryImpl
    ): GitHubUserRepository
}
