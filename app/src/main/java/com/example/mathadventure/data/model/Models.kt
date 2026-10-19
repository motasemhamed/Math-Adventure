package com.example.mathadventure.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class LearningTier(val displayNameEn: String, val displayNameAr: String) {
    BEGINNER("Junior Cadet (Ages 5-7)", "المستكشف الصغير (٥-٧ سنوات)"),
    INTERMEDIATE("Lab Explorer (Ages 8-10)", "مستكشف المعمل (٨-١٠ سنوات)"),
    EXPERT("Quantum Master (Ages 10-12)", "عالم كوانتم (١٠-١٢ سنة)")
}

enum class MathOperation {
    MULTIPLICATION,
    DIVISION,
    MIXED
}

@Entity(tableName = "child_profiles")
data class ChildProfile(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val avatarId: String = "robot_default",
    val tier: LearningTier = LearningTier.BEGINNER,
    val stars: Int = 10,
    val coins: Int = 50,
    val currentStreak: Int = 1,
    val unlockedSkins: String = "robot_default",
    val selectedSkin: String = "robot_default",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "topic_progress")
data class TopicProgress(
    @PrimaryKey val topicKey: String,
    val profileId: Long,
    val operation: MathOperation,
    val totalAttempts: Int = 0,
    val correctCount: Int = 0,
    val masteryPercentage: Int = 0, // 0 to 100
    val isMastered: Boolean = false,
    val lastPracticed: Long = System.currentTimeMillis()
)

@Entity(tableName = "assessment_results")
data class AssessmentResult(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val profileId: Long,
    val testType: String, // "DIAGNOSTIC", "TABLE_QUIZ", "MASTERY_FINAL", "MISTAKE_REVIEW", "BOSS"
    val score: Int,
    val totalQuestions: Int,
    val durationSeconds: Int,
    val weakTopics: String = "", // comma-separated topic keys
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "unlocked_achievements")
data class UnlockedAchievement(
    @PrimaryKey val badgeId: String,
    val profileId: Long,
    val unlockedAt: Long = System.currentTimeMillis()
)

data class BadgeDefinition(
    val id: String,
    val titleEn: String,
    val titleAr: String,
    val descEn: String,
    val descAr: String,
    val emoji: String,
    val requiredStars: Int
)

object BadgesCatalog {
    val ALL = listOf(
        BadgeDefinition("first_spark", "First Spark", "الشرارة الأولى", "Solved your very first math question!", "حل أول مسألة رياضية!", "⚡", 5),
        BadgeDefinition("times_two", "Double Trouble", "أستاذ جدول ٢", "Mastered the 2× multiplication table!", "إتقان جدول ضرب ٢!", "✌️", 15),
        BadgeDefinition("equal_sharer", "Fair Sharer", "المقسّم العادل", "Completed equal sharing division academy!", "إكمال أكاديمية القسمة العادلة!", "🍰", 25),
        BadgeDefinition("fact_connector", "Family Builder", "بطل عائلات الأعداد", "Connected multiplication and division fact families!", "ربط عائلات الضرب والقسمة!", "🔗", 40),
        BadgeDefinition("rocket_ace", "Cosmic Pilot", "طيار الفضاء", "Fueled and launched Rocket Math!", "تزويد الصاروخ بالوقود وإطلاقه!", "🚀", 55),
        BadgeDefinition("panda_shopper", "Fruit Master", "بائع الفواكه", "Packaged fruit boxes in Panda's Shop!", "تجهيز علب الفواكه في متجر الباندا!", "🐼", 70),
        BadgeDefinition("bridge_engineer", "Bridge Hero", "مهندس الجسور", "Constructed math bridges for animal friends!", "بناء جسور الأرقام للأصدقاء!", "🌉", 85),
        BadgeDefinition("long_div_detective", "Algorithm Ace", "خبير القسمة المطولة", "Solved step-by-step long division without errors!", "حل القسمة المطولة خطوة بخطوة بنجاح!", "🔍", 100),
        BadgeDefinition("quantum_champion", "Quantum Champion", "بطل كوانتم الخارق", "Defeated the Quantum Boss Challenge!", "الفوز في تحدي الزعيم كوانتم!", "👑", 150)
    )
}
