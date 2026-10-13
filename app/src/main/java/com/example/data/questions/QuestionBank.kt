package com.example.data.questions

import com.example.core.model.AnswerOption
import com.example.core.model.GameLevel
import com.example.core.model.Question
import com.example.core.model.QuestionType
import com.example.core.model.VisualItemType

object QuestionBank {

    private val forestItems = listOf(
        VisualItemType.LEAF,
        VisualItemType.ACORN,
        VisualItemType.MUSHROOM,
        VisualItemType.PINE_CONE,
        VisualItemType.WOOD_LOG,
        VisualItemType.FOREST_BERRY
    )

    private val fruitItems = listOf(
        VisualItemType.APPLE,
        VisualItemType.BANANA,
        VisualItemType.ORANGE,
        VisualItemType.STRAWBERRY,
        VisualItemType.WATERMELON,
        VisualItemType.GRAPES,
        VisualItemType.MANGO
    )

    private val vegetableItems = listOf(
        VisualItemType.CARROT,
        VisualItemType.TOMATO,
        VisualItemType.CORN,
        VisualItemType.BROCCOLI,
        VisualItemType.EGGPLANT,
        VisualItemType.POTATO
    )

    private val animalItems = listOf(
        VisualItemType.BUNNY,
        VisualItemType.ELEPHANT,
        VisualItemType.BIRD,
        VisualItemType.FISH,
        VisualItemType.DUCK,
        VisualItemType.TURTLE,
        VisualItemType.BUTTERFLY,
        VisualItemType.CAT,
        VisualItemType.CHICK,
        VisualItemType.MONKEY
    )

    private val plantItems = listOf(
        VisualItemType.SUNFLOWER,
        VisualItemType.ROSE,
        VisualItemType.SPROUT,
        VisualItemType.TREE,
        VisualItemType.CLOVER,
        VisualItemType.LEAF_MONSTERA
    )

    private val allLevelsCache: List<GameLevel> by lazy {
        (1..200).map { lvl -> generateLevel(lvl) }
    }

    fun getAllLevels(): List<GameLevel> = allLevelsCache

    fun getLevel(levelNumber: Int): GameLevel {
        val safeIndex = (levelNumber - 1).coerceIn(0, 199)
        return allLevelsCache[safeIndex]
    }

    fun getLevelsForWorld(worldId: Int): List<GameLevel> {
        return allLevelsCache.filter { it.worldId == worldId }
    }

    private fun generateLevel(levelNumber: Int): GameLevel {
        val worldId = when {
            levelNumber <= 40 -> 1
            levelNumber <= 80 -> 2
            levelNumber <= 120 -> 3
            levelNumber <= 160 -> 4
            else -> 5
        }

        val isBoss = (levelNumber % 40 == 0)
        val levelIndexInWorld = ((levelNumber - 1) % 40) + 1

        val title = if (isBoss) {
            when (worldId) {
                1 -> "Tantangan Boss: Penjaga Hutan Angka"
                2 -> "Tantangan Boss: Pesta Panen Buah Kiko"
                3 -> "Tantangan Boss: Raja Kebun Sayur"
                4 -> "Tantangan Boss: Festival Kerajaan Hewan"
                else -> "Tantangan Terakhir: Sang Juara Dunia Ceria"
            }
        } else {
            "Level $levelNumber"
        }

        val questions = (1..5).map { qIndex ->
            generateQuestion(worldId, levelNumber, levelIndexInWorld, qIndex, isBoss)
        }

        return GameLevel(
            levelNumber = levelNumber,
            worldId = worldId,
            title = title,
            isBossLevel = isBoss,
            questions = questions
        )
    }

    private fun generateQuestion(
        worldId: Int,
        levelNumber: Int,
        levelInWorld: Int,
        qIndex: Int,
        isBoss: Boolean
    ): Question {
        val qId = "w${worldId}_l${levelNumber}_q${qIndex}"

        val worldItems = when (worldId) {
            1 -> forestItems
            2 -> fruitItems
            3 -> vegetableItems
            4 -> animalItems
            else -> plantItems
        }

        val item = worldItems[(levelNumber + qIndex) % worldItems.size]

        // Determine question type based on progression
        val qType = when {
            isBoss -> {
                when (qIndex) {
                    1 -> QuestionType.COUNT_OBJECTS
                    2 -> QuestionType.ADDITION_VISUAL
                    3 -> QuestionType.MATCH_NAME
                    4 -> QuestionType.ADDITION_VISUAL
                    else -> QuestionType.BOSS_CHALLENGE
                }
            }
            levelInWorld in 1..15 -> {
                if (qIndex == 5 && levelInWorld >= 6) QuestionType.MATCH_NAME else QuestionType.COUNT_OBJECTS
            }
            levelInWorld in 16..25 -> {
                when (qIndex) {
                    1, 2 -> QuestionType.COUNT_OBJECTS
                    3 -> QuestionType.MATCH_NAME
                    else -> QuestionType.ADDITION_VISUAL
                }
            }
            levelInWorld in 26..35 -> {
                when (qIndex) {
                    1 -> QuestionType.COUNT_OBJECTS
                    2, 3 -> QuestionType.ADDITION_VISUAL
                    4 -> QuestionType.MATCH_NAME
                    else -> QuestionType.ADDITION_VISUAL
                }
            }
            else -> { // 36..39
                when (qIndex) {
                    1 -> QuestionType.COUNT_OBJECTS
                    2, 4 -> QuestionType.ADDITION_VISUAL
                    3 -> QuestionType.MATCH_NAME
                    else -> QuestionType.PATTERN_COMPLETION
                }
            }
        }

        return when (qType) {
            QuestionType.COUNT_OBJECTS -> {
                // Determine count based on difficulty
                val maxCount = when {
                    levelInWorld <= 10 -> 5
                    levelInWorld <= 25 -> 7
                    else -> 10
                }
                val count = ((levelNumber * 3 + qIndex * 2) % maxCount) + 1
                val instruction = "Hitung ada berapa ${item.labelId} di atas?"
                val spoken = "Ayo hitung, ada berapa ${item.labelId}?"
                val options = generateNumericOptions(count, max = 10, optionCount = if (levelNumber <= 20) 3 else 4)

                Question(
                    id = qId,
                    worldId = worldId,
                    levelNumber = levelNumber,
                    questionIndex = qIndex,
                    instruction = instruction,
                    spokenPrompt = spoken,
                    questionType = qType,
                    visualItem = item,
                    firstGroupCount = count,
                    secondGroupCount = 0,
                    options = options,
                    correctAnswerText = count.toString(),
                    praiseFeedback = getPraiseText(qIndex),
                    hintExplanation = "Sentuh setiap ${item.labelId} dengan jarimu untuk menghitungnya!"
                )
            }

            QuestionType.ADDITION_VISUAL, QuestionType.BOSS_CHALLENGE -> {
                val group1 = ((levelNumber + qIndex) % 4) + 1
                val group2 = ((levelNumber * 2 + qIndex) % 4) + 1
                val sum = group1 + group2
                val instruction = "Berapa $group1 ${item.labelId} ditambah $group2 ${item.labelId}?"
                val spoken = "Berapa hasil dari $group1 ditambah $group2?"
                val options = generateNumericOptions(sum, max = 10, optionCount = if (levelNumber <= 30) 3 else 4)

                Question(
                    id = qId,
                    worldId = worldId,
                    levelNumber = levelNumber,
                    questionIndex = qIndex,
                    instruction = instruction,
                    spokenPrompt = spoken,
                    questionType = QuestionType.ADDITION_VISUAL,
                    visualItem = item,
                    firstGroupCount = group1,
                    secondGroupCount = group2,
                    options = options,
                    correctAnswerText = sum.toString(),
                    praiseFeedback = getPraiseText(qIndex),
                    hintExplanation = "Gabungkan kedua kelompok ${item.labelId} lalu hitung semuanya!"
                )
            }

            QuestionType.MATCH_NAME -> {
                val instruction = "Manakah gambar ${item.labelId}?"
                val spoken = "Sentuh pilihan yang bertuliskan ${item.labelId}!"

                // Distractor items from the same world
                val otherItems = worldItems.filter { it != item }
                val distractor1 = otherItems[qIndex % otherItems.size]
                val distractor2 = otherItems[(qIndex + 1) % otherItems.size]

                val rawOptions = listOf(
                    item.labelId to true,
                    distractor1.labelId to false,
                    distractor2.labelId to false
                ).shuffled()

                val options = rawOptions.mapIndexed { idx, pair ->
                    AnswerOption(
                        id = "${qId}_opt_$idx",
                        text = pair.first,
                        isCorrect = pair.second
                    )
                }

                Question(
                    id = qId,
                    worldId = worldId,
                    levelNumber = levelNumber,
                    questionIndex = qIndex,
                    instruction = instruction,
                    spokenPrompt = spoken,
                    questionType = QuestionType.MATCH_NAME,
                    visualItem = item,
                    firstGroupCount = 1,
                    secondGroupCount = 0,
                    options = options,
                    correctAnswerText = item.labelId,
                    praiseFeedback = getPraiseText(qIndex),
                    hintExplanation = "Perhatikan bentuk dan warna ${item.labelId}!"
                )
            }

            QuestionType.PATTERN_COMPLETION -> {
                val nextCount = ((levelNumber + qIndex) % 3) + 2
                val instruction = "Lengkapi pola jumlah: berapa berikutnya?"
                val spoken = "Berapa jumlah berikutnya dalam pola?"
                val options = generateNumericOptions(nextCount, max = 8, optionCount = 3)

                Question(
                    id = qId,
                    worldId = worldId,
                    levelNumber = levelNumber,
                    questionIndex = qIndex,
                    instruction = instruction,
                    spokenPrompt = spoken,
                    questionType = QuestionType.PATTERN_COMPLETION,
                    visualItem = item,
                    firstGroupCount = nextCount,
                    secondGroupCount = 0,
                    options = options,
                    correctAnswerText = nextCount.toString(),
                    praiseFeedback = getPraiseText(qIndex),
                    hintExplanation = "Perhatikan urutan jumlahnya bersama Kiko!"
                )
            }

            else -> {
                // Fallback safe counting
                val count = ((levelNumber + qIndex) % 5) + 1
                val options = generateNumericOptions(count, max = 7, optionCount = 3)
                Question(
                    id = qId,
                    worldId = worldId,
                    levelNumber = levelNumber,
                    questionIndex = qIndex,
                    instruction = "Hitung ada berapa ${item.labelId}?",
                    spokenPrompt = "Ayo hitung ada berapa ${item.labelId}!",
                    questionType = QuestionType.COUNT_OBJECTS,
                    visualItem = item,
                    firstGroupCount = count,
                    secondGroupCount = 0,
                    options = options,
                    correctAnswerText = count.toString(),
                    praiseFeedback = getPraiseText(qIndex),
                    hintExplanation = "Hitung satu demi satu!"
                )
            }
        }
    }

    private fun generateNumericOptions(correctValue: Int, max: Int, optionCount: Int = 3): List<AnswerOption> {
        val distinctNumbers = mutableSetOf(correctValue)

        // Try offsets
        val candidates = listOf(
            correctValue - 1,
            correctValue + 1,
            correctValue - 2,
            correctValue + 2,
            correctValue + 3,
            correctValue - 3
        ).filter { it in 1..max && it != correctValue }

        for (cand in candidates) {
            distinctNumbers.add(cand)
            if (distinctNumbers.size == optionCount) break
        }

        // Fill if still needed
        var fill = 1
        while (distinctNumbers.size < optionCount && fill <= max) {
            distinctNumbers.add(fill)
            fill++
        }

        val shuffled = distinctNumbers.toList().shuffled()
        return shuffled.mapIndexed { index, num ->
            AnswerOption(
                id = "opt_${index}_$num",
                text = num.toString(),
                numberValue = num,
                isCorrect = (num == correctValue)
            )
        }
    }

    private fun getPraiseText(index: Int): String {
        return when (index % 5) {
            0 -> "Hebat sekali! Jawabanmu benar!"
            1 -> "Pintar sekali! Kiko bangga padamu!"
            2 -> "Luar biasa! Kamu semakin pintar!"
            3 -> "Bagus sekali! Lanjutkan petualangan!"
            else -> "Hore! Kamu berhasil!"
        }
    }
}
