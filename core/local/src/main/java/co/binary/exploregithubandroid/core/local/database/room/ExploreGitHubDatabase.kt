package co.binary.exploregithubandroid.core.local.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        RecentSearchEntity::class,
    ],
    version = 1,
)
abstract class ExploreGitHubDatabase : RoomDatabase() {
    abstract fun recentSearchDao(): RecentSearchDao

    companion object {
        private const val DATABASE_NAME = "explore_github_database"

        fun buildDatabase(context: Context): ExploreGitHubDatabase {
            return Room.databaseBuilder(
                context = context,
                klass = ExploreGitHubDatabase::class.java,
                name = DATABASE_NAME
            ).build()
        }
    }
}
