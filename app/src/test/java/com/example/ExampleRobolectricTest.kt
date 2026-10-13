package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.GamePreferencesRepository
import com.example.data.questions.QuestionBank
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Aku Pintar", appName)
    }

    @Test
    fun `test game preferences data store persistence`() = runTest {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repo = GamePreferencesRepository(context)

        // Initial progress
        val initialProgress = repo.playerProgressFlow.first()
        assertEquals(1, initialProgress.highestUnlockedLevel)
        assertEquals(1, initialProgress.highestUnlockedWorld)

        // Complete level 1 with 3 stars
        repo.recordLevelCompleted(levelNumber = 1, starsEarned = 3)
        val updatedProgress = repo.playerProgressFlow.first()
        assertEquals(2, updatedProgress.highestUnlockedLevel)
        assertEquals(3, updatedProgress.getStarsForLevel(1))
    }
}
