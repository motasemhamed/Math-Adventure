package com.example.mathadventure.data.repository

import com.example.mathadventure.data.db.AppDatabase
import com.example.mathadventure.data.model.AssessmentResult
import com.example.mathadventure.data.model.BadgesCatalog
import com.example.mathadventure.data.model.ChildProfile
import com.example.mathadventure.data.model.LearningTier
import com.example.mathadventure.data.model.MathOperation
import com.example.mathadventure.data.model.TopicProgress
import com.example.mathadventure.data.model.UnlockedAchievement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class MathRepository(private val database: AppDatabase) {

    val allProfiles: Flow<List<ChildProfile>> = database.childProfileDao().getAllProfiles()

    suspend fun getOrCreateActiveProfile(): ChildProfile {
        val existing = database.childProfileDao().getFirstProfile()
        if (existing != null) return existing

        val starter = ChildProfile(
            name = "Sami / سامي",
            tier = LearningTier.BEGINNER,
            stars = 20,
            coins = 100
        )
        val id = database.childProfileDao().insertProfile(starter)
        return starter.copy(id = id)
    }

    suspend fun updateProfile(profile: ChildProfile) {
        database.childProfileDao().updateProfile(profile)
    }

    suspend fun addRewards(profileId: Long, stars: Int, coins: Int) {
        database.childProfileDao().addRewards(profileId, stars, coins)
        checkAchievements(profileId)
    }

    suspend fun recordTopicAnswer(
        profileId: Long,
        topicKey: String,
        operation: MathOperation,
        isCorrect: Boolean
    ) {
        val current = database.topicProgressDao().getTopicProgress(profileId, topicKey)
            ?: TopicProgress(
                topicKey = topicKey,
                profileId = profileId,
                operation = operation
            )

        val newTotal = current.totalAttempts + 1
        val newCorrect = current.correctCount + if (isCorrect) 1 else 0
        val percentage = ((newCorrect.toDouble() / newTotal) * 100).toInt()
        val mastered = newTotal >= 5 && percentage >= 85

        val updated = current.copy(
            totalAttempts = newTotal,
            correctCount = newCorrect,
            masteryPercentage = percentage,
            isMastered = mastered,
            lastPracticed = System.currentTimeMillis()
        )
        database.topicProgressDao().insertOrUpdate(updated)
    }

    fun getProgressForProfile(profileId: Long): Flow<List<TopicProgress>> {
        return database.topicProgressDao().getProgressForProfile(profileId)
    }

    suspend fun recordAssessmentResult(
        profileId: Long,
        testType: String,
        score: Int,
        total: Int,
        durationSeconds: Int,
        weakTopics: String
    ): Long {
        val res = AssessmentResult(
            profileId = profileId,
            testType = testType,
            score = score,
            totalQuestions = total,
            durationSeconds = durationSeconds,
            weakTopics = weakTopics
        )
        val id = database.assessmentDao().insertResult(res)
        checkAchievements(profileId)
        return id
    }

    fun getAssessmentResults(profileId: Long): Flow<List<AssessmentResult>> {
        return database.assessmentDao().getResultsForProfile(profileId)
    }

    fun getUnlockedAchievements(profileId: Long): Flow<List<UnlockedAchievement>> {
        return database.achievementDao().getUnlockedForProfile(profileId)
    }

    suspend fun checkAchievements(profileId: Long) {
        val profile = database.childProfileDao().getProfileById(profileId) ?: return
        for (badge in BadgesCatalog.ALL) {
            if (profile.stars >= badge.requiredStars) {
                database.achievementDao().unlockBadge(
                    UnlockedAchievement(badgeId = badge.id, profileId = profileId)
                )
            }
        }
    }

    suspend fun resetAllData(profileId: Long) {
        database.childProfileDao().clearAllProfiles()
        database.topicProgressDao().clearProgressForProfile(profileId)
        database.assessmentDao().clearResultsForProfile(profileId)
        database.achievementDao().clearAchievementsForProfile(profileId)
    }
}
