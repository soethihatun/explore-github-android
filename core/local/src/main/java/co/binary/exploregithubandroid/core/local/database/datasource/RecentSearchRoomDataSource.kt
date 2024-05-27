package co.binary.exploregithubandroid.core.local.database.datasource

import co.binary.exploregithubandroid.core.local.database.room.RecentSearchDao
import co.binary.exploregithubandroid.core.local.database.room.RecentSearchEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class RecentSearchRoomDataSource @Inject constructor(
    private val recentSearchDao: RecentSearchDao
) : RecentSearchLocalDataSource {
    override suspend fun addRecentSearch(query: String) {
        recentSearchDao.insertOrReplaceRecentSearch(RecentSearchEntity(query))
    }

    override fun getRecentSearchStream(limit: Int): Flow<List<String>> =
        recentSearchDao.getRecentSearchStream(limit)

    override suspend fun clearRecentSearches() {
        recentSearchDao.clearRecentSearches()
    }
}
