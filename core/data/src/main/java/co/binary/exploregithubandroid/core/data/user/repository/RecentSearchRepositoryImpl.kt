package co.binary.exploregithubandroid.core.data.user.repository

import co.binary.exploregithubandroid.core.local.database.datasource.RecentSearchLocalDataSource
import co.binary.exploregithubandroid.core.repository.user.RecentSearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class RecentSearchRepositoryImpl @Inject constructor(
    private val local: RecentSearchLocalDataSource,
) : RecentSearchRepository {
    override suspend fun insertOrReplaceRecentSearch(query: String) {
        local.addRecentSearch(query)
    }

    override fun getRecentSearchStream(limit: Int): Flow<List<String>> =
        local.getRecentSearchStream(limit)

    override suspend fun clearRecentSearches() =
        local.clearRecentSearches()
}
