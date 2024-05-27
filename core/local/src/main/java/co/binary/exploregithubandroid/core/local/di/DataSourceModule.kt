package co.binary.exploregithubandroid.core.local.di

import co.binary.exploregithubandroid.core.local.database.datasource.RecentSearchLocalDataSource
import co.binary.exploregithubandroid.core.local.database.datasource.RecentSearchRoomDataSource
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
    abstract fun bindsRecentSearchLocalDataSource(
        impl: RecentSearchRoomDataSource,
    ): RecentSearchLocalDataSource
}
