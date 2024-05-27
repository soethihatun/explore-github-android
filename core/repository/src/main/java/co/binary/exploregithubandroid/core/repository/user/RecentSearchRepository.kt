package co.binary.exploregithubandroid.core.repository.user

import kotlinx.coroutines.flow.Flow

interface RecentSearchRepository {
    suspend fun insertOrReplaceRecentSearch(query: String)

    fun getRecentSearchStream(limit: Int): Flow<List<String>>

    suspend fun clearRecentSearches()
}
