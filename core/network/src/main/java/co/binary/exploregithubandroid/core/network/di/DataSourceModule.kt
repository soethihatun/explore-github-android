package co.binary.exploregithubandroid.core.network.di

import co.binary.exploregithubandroid.core.network.user.datasource.GitHubUserNetworkDataSource
import co.binary.exploregithubandroid.core.network.user.datasource.GitHubUserNetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun bindsGitHubUserNetworkDataSource(
        impl: GitHubUserNetworkDataSourceImpl,
    ): GitHubUserNetworkDataSource
}
