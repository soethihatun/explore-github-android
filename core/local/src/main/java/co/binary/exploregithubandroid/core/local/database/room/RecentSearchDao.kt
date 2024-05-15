package co.binary.exploregithubandroid.core.local.database.room

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RecentSearchDao {
    @Upsert
    abstract suspend fun insertOrReplaceRecentSearch(entity: RecentSearchEntity)

    @Query("SELECT `query` FROM recent_searches ORDER BY created_at DESC LIMIT :limit")
    abstract fun getRecentSearchStream(limit: Int): Flow<List<String>>

    @Query(value = "DELETE FROM recent_searches")
    abstract fun clearRecentSearches()
}
