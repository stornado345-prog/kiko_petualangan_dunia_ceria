package com.example

import com.example.core.model.PlayerProgress
import com.example.core.model.QuestionType
import com.example.data.questions.QuestionBank
import com.example.data.questions.WorldCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun verifyTotalWorldsAndRanges() {
        val worlds = WorldCatalog.worlds
        assertEquals("Harus terdapat tepat 5 dunia", 5, worlds.size)

        assertEquals("Dunia 1 start 1, end 40", 1..40, worlds[0].startLevel..worlds[0].endLevel)
        assertEquals("Dunia 2 start 41, end 80", 41..80, worlds[1].startLevel..worlds[1].endLevel)
        assertEquals("Dunia 3 start 81, end 120", 81..120, worlds[2].startLevel..worlds[2].endLevel)
        assertEquals("Dunia 4 start 121, end 160", 121..160, worlds[3].startLevel..worlds[3].endLevel)
        assertEquals("Dunia 5 start 161, end 200", 161..200, worlds[4].startLevel..worlds[4].endLevel)
    }

    @Test
    fun verifyExactly200LevelsAnd1000Questions() {
        val allLevels = QuestionBank.getAllLevels()
        assertEquals("Harus terdapat tepat 200 level", 200, allLevels.size)

        var totalQuestions = 0
        val questionIds = mutableSetOf<String>()

        allLevels.forEachIndexed { index, level ->
            val expectedLevelNumber = index + 1
            assertEquals("Level number harus berurutan 1..200", expectedLevelNumber, level.levelNumber)
            assertTrue("Setiap level minimal memiliki 5 pertanyaan", level.questions.size >= 5)
            totalQuestions += level.questions.size

            // Check Boss level marking
            if (expectedLevelNumber % 40 == 0) {
                assertTrue("Level $expectedLevelNumber harus ditandai sebagai Boss Level", level.isBossLevel)
            }

            // Verify each question
            level.questions.forEach { q ->
                // Unique ID check
                assertTrue("Question ID harus unik: ${q.id}", questionIds.add(q.id))
                assertTrue("Instruction tidak boleh kosong", q.instruction.isNotBlank())
                assertTrue("Spoken prompt tidak boleh kosong", q.spokenPrompt.isNotBlank())

                // Options verification
                assertTrue("Setiap pertanyaan harus memiliki minimal 2 opsi", q.options.size >= 2)
                val correctCount = q.options.count { it.isCorrect }
                assertEquals("Harus ada tepat 1 jawaban benar pada ${q.id}", 1, correctCount)

                val correctOption = q.options.first { it.isCorrect }
                assertEquals(
                    "Teks opsi benar harus sama dengan correctAnswerText pada ${q.id}",
                    q.correctAnswerText,
                    correctOption.text
                )

                // Check no duplicate options
                val distinctTexts = q.options.map { it.text }.toSet()
                assertEquals("Opsi jawaban tidak boleh duplikat pada ${q.id}", q.options.size, distinctTexts.size)

                // Check positive counts
                assertTrue("First group count harus > 0 pada ${q.id}", q.firstGroupCount > 0)
                if (q.questionType == QuestionType.ADDITION_VISUAL) {
                    assertTrue("Second group count harus > 0 untuk penjumlahan pada ${q.id}", q.secondGroupCount > 0)
                    val expectedSum = q.firstGroupCount + q.secondGroupCount
                    assertEquals(
                        "Hasil penjumlahan harus sesuai",
                        expectedSum.toString(),
                        q.correctAnswerText
                    )
                }
            }
        }

        assertTrue("Total pertanyaan harus minimal 1.000 (aktual: $totalQuestions)", totalQuestions >= 1000)
    }

    @Test
    fun verifyBadgesMilestones() {
        val badges = WorldCatalog.badges
        assertEquals("Harus ada 20 lencana prestasi", 20, badges.size)

        val requiredLevels = badges.map { it.requiredLevel }
        for (milestone in 10..200 step 10) {
            assertTrue("Harus ada badge untuk level $milestone", requiredLevels.contains(milestone))
        }
    }

    @Test
    fun verifyPlayerProgressLogic() {
        val progress = PlayerProgress(
            highestUnlockedLevel = 10,
            highestUnlockedWorld = 1,
            starsByLevel = mapOf(1 to 3, 2 to 2, 3 to 3)
        )

        assertTrue("Level 1 harus terbuka", progress.isLevelUnlocked(1))
        assertTrue("Level 10 harus terbuka", progress.isLevelUnlocked(10))
        assertTrue("Level 11 harus terkunci", !progress.isLevelUnlocked(11))

        assertEquals(3, progress.getStarsForLevel(1))
        assertEquals(2, progress.getStarsForLevel(2))
        assertEquals(0, progress.getStarsForLevel(4))
    }
}
