package co.binary.exploregithubandroid.core.local.database.datasource

import kotlinx.coroutines.flow.Flow

interface RecentSearchLocalDataSource {
    suspend fun addRecentSearch(query: String)

    fun getRecentSearchStream(limit: Int): Flow<List<String>>

    suspend fun clearRecentSearches()
}
