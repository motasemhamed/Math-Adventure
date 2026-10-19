package com.example.mathadventure.data.db

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.Update
import com.example.mathadventure.data.model.AssessmentResult
import com.example.mathadventure.data.model.ChildProfile
import com.example.mathadventure.data.model.LearningTier
import com.example.mathadventure.data.model.MathOperation
import com.example.mathadventure.data.model.TopicProgress
import com.example.mathadventure.data.model.UnlockedAchievement
import kotlinx.coroutines.flow.Flow

class Converters {
    @TypeConverter
    fun fromTier(tier: LearningTier): String = tier.name

    @TypeConverter
    fun toTier(name: String): LearningTier = runCatching { LearningTier.valueOf(name) }.getOrDefault(LearningTier.BEGINNER)

    @TypeConverter
    fun fromOperation(op: MathOperation): String = op.name

    @TypeConverter
    fun toOperation(name: String): MathOperation = runCatching { MathOperation.valueOf(name) }.getOrDefault(MathOperation.MULTIPLICATION)
}

@Dao
interface ChildProfileDao {
    @Query("SELECT * FROM child_profiles ORDER BY id ASC")
    fun getAllProfiles(): Flow<List<ChildProfile>>

    @Query("SELECT * FROM child_profiles WHERE id = :id LIMIT 1")
    suspend fun getProfileById(id: Long): ChildProfile?

    @Query("SELECT * FROM child_profiles ORDER BY id ASC LIMIT 1")
    suspend fun getFirstProfile(): ChildProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: ChildProfile): Long

    @Update
    suspend fun updateProfile(profile: ChildProfile)

    @Query("UPDATE child_profiles SET stars = stars + :starDelta, coins = coins + :coinDelta WHERE id = :profileId")
    suspend fun addRewards(profileId: Long, starDelta: Int, coinDelta: Int)

    @Query("DELETE FROM child_profiles WHERE id = :id")
    suspend fun deleteProfile(id: Long)

    @Query("DELETE FROM child_profiles")
    suspend fun clearAllProfiles()
}

@Dao
interface TopicProgressDao {
    @Query("SELECT * FROM topic_progress WHERE profileId = :profileId")
    fun getProgressForProfile(profileId: Long): Flow<List<TopicProgress>>

    @Query("SELECT * FROM topic_progress WHERE profileId = :profileId AND topicKey = :topicKey LIMIT 1")
    suspend fun getTopicProgress(profileId: Long, topicKey: String): TopicProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(progress: TopicProgress)

    @Query("SELECT * FROM topic_progress WHERE profileId = :profileId AND masteryPercentage < 80 ORDER BY masteryPercentage ASC")
    fun getWeakTopics(profileId: Long): Flow<List<TopicProgress>>

    @Query("DELETE FROM topic_progress WHERE profileId = :profileId")
    suspend fun clearProgressForProfile(profileId: Long)
}

@Dao
interface AssessmentDao {
    @Insert
    suspend fun insertResult(result: AssessmentResult): Long

    @Query("SELECT * FROM assessment_results WHERE profileId = :profileId ORDER BY timestamp DESC")
    fun getResultsForProfile(profileId: Long): Flow<List<AssessmentResult>>

    @Query("DELETE FROM assessment_results WHERE profileId = :profileId")
    suspend fun clearResultsForProfile(profileId: Long)
}

@Dao
interface AchievementDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun unlockBadge(badge: UnlockedAchievement)

    @Query("SELECT * FROM unlocked_achievements WHERE profileId = :profileId")
    fun getUnlockedForProfile(profileId: Long): Flow<List<UnlockedAchievement>>

    @Query("DELETE FROM unlocked_achievements WHERE profileId = :profileId")
    suspend fun clearAchievementsForProfile(profileId: Long)
}

@Database(
    entities = [ChildProfile::class, TopicProgress::class, AssessmentResult::class, UnlockedAchievement::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun childProfileDao(): ChildProfileDao
    abstract fun topicProgressDao(): TopicProgressDao
    abstract fun assessmentDao(): AssessmentDao
    abstract fun achievementDao(): AchievementDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "math_adventure.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
