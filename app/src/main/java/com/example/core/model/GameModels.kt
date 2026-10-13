package com.example.core.model

enum class VisualItemType(val labelId: String) {
    // World 1 - Hutan Angka
    ACORN("Biji Kenari"),
    MUSHROOM("Jamur"),
    LEAF("Daun"),
    PINE_CONE("Buah Pinus"),
    WOOD_LOG("Kayu"),
    FOREST_BERRY("Buah Hutan"),

    // World 2 - Kebun Buah Ceria
    APPLE("Apel"),
    BANANA("Pisang"),
    ORANGE("Jeruk"),
    STRAWBERRY("Stroberi"),
    WATERMELON("Semangka"),
    GRAPES("Anggur"),
    MANGO("Mangga"),

    // World 3 - Ladang Sayuran
    CARROT("Wortel"),
    TOMATO("Tomat"),
    CORN("Jagung"),
    BROCCOLI("Brokoli"),
    EGGPLANT("Terong"),
    POTATO("Kentang"),

    // World 4 - Kerajaan Hewan
    BUNNY("Kelinci"),
    ELEPHANT("Gajah"),
    BIRD("Burung"),
    FISH("Ikan"),
    DUCK("Bebek"),
    TURTLE("Kura-Kura"),
    BUTTERFLY("Kupu-Kupu"),
    CAT("Kucing"),
    CHICK("Anak Ayam"),
    MONKEY("Monyet"),

    // World 5 - Hutan Tumbuhan Ajaib
    SUNFLOWER("Bunga Matahari"),
    ROSE("Bunga Mawar"),
    SPROUT("Tunas Kecil"),
    TREE("Pohon Rindang"),
    CLOVER("Semanggi Beruntung"),
    LEAF_MONSTERA("Daun Lebar")
}

enum class QuestionType {
    COUNT_OBJECTS,
    ADDITION_VISUAL,
    COMPARE_COUNT,
    MATCH_NAME,
    PATTERN_COMPLETION,
    BOSS_CHALLENGE
}

data class AnswerOption(
    val id: String,
    val text: String,
    val numberValue: Int? = null,
    val isCorrect: Boolean = false
)

data class Question(
    val id: String,
    val worldId: Int,
    val levelNumber: Int,
    val questionIndex: Int, // 1..5
    val instruction: String,
    val spokenPrompt: String,
    val questionType: QuestionType,
    val visualItem: VisualItemType,
    val firstGroupCount: Int,
    val secondGroupCount: Int = 0,
    val options: List<AnswerOption>,
    val correctAnswerText: String,
    val praiseFeedback: String = "Hebat sekali!",
    val hintExplanation: String = "Ayo hitung perlahan bersama Kiko!"
)

data class GameLevel(
    val levelNumber: Int, // 1..200
    val worldId: Int, // 1..5
    val title: String,
    val isBossLevel: Boolean,
    val questions: List<Question>
)

data class WorldInfo(
    val id: Int,
    val name: String,
    val subtitle: String,
    val description: String,
    val primaryColor: Long,
    val secondaryColor: Long,
    val startLevel: Int,
    val endLevel: Int,
    val totalLevels: Int = 40,
    val badgeTitle: String
)

data class BadgeReward(
    val id: String,
    val title: String,
    val description: String,
    val requiredLevel: Int,
    val worldId: Int,
    val iconEmoji: String
)

data class PlayerProgress(
    val highestUnlockedLevel: Int = 1,
    val highestUnlockedWorld: Int = 1,
    val starsByLevel: Map<Int, Int> = emptyMap(),
    val totalStars: Int = 0,
    val soundEnabled: Boolean = true,
    val musicEnabled: Boolean = true,
    val ttsEnabled: Boolean = true
) {
    fun isLevelUnlocked(level: Int): Boolean = level <= highestUnlockedLevel
    fun isWorldUnlocked(world: Int): Boolean = world <= highestUnlockedWorld
    fun getStarsForLevel(level: Int): Int = starsByLevel[level] ?: 0
}
