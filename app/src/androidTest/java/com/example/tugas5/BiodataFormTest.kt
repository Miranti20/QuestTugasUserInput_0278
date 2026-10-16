package com.example.tugas5

import android.graphics.Bitmap
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class BiodataFormTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test fun fillFormValidateRestoreAndProceed() {
        compose.onNodeWithTag("proceed").assertIsNotEnabled()
        capture("form-kosong.png")
        compose.onNodeWithTag("firstName").performTextInput("Ranti")
        compose.onNodeWithTag("lastName").performTextInput("Mahasiswa")
        compose.onNodeWithTag("email").performTextInput("email-salah")
        compose.onNodeWithText(compose.activity.getString(R.string.email_invalid)).assertExists()
        compose.onNodeWithTag("proceed").assertIsNotEnabled()
        compose.onNodeWithTag("email").performTextReplacement("ranti@example.com")
        compose.onNodeWithTag("birthDate").performScrollTo().performClick()
        onView(withId(android.R.id.button1)).perform(click())
        compose.onNodeWithTag("experience").performScrollTo().performClick()
        val experience = compose.activity.resources.getStringArray(R.array.experience_options)[1]
        compose.onNodeWithText(experience).performClick()
        compose.onNodeWithTag("skillAndroid").performScrollTo().performClick()
        compose.onNodeWithTag("skillUiUx").performClick()
        compose.onNodeWithTag("proceed").performScrollTo().assertIsEnabled()
        compose.activityRule.scenario.recreate()
        compose.onNodeWithTag("firstName").assertTextEquals("Ranti")
        compose.onNodeWithTag("email").assertTextEquals("ranti@example.com")
        compose.onNodeWithTag("experience").assertTextEquals(experience)
        compose.onNodeWithTag("skillAndroid").assertIsOn()
        compose.onNodeWithTag("skillUiUx").assertIsOn()
        compose.onNodeWithTag("skillWeb").assertIsOff()
        compose.onNodeWithTag("proceed").performScrollTo().assertIsEnabled()
        capture("form-terisi.png")
        compose.onNodeWithTag("proceed").performClick()
        compose.onNodeWithText(compose.activity.getString(R.string.summary_title)).assertIsDisplayed()
        compose.onNodeWithText(compose.activity.getString(R.string.summary_email, "ranti@example.com")).assertIsDisplayed()
        capture("ringkasan-biodata.png")
        compose.onNodeWithText(compose.activity.getString(R.string.btn_close)).performClick()
        compose.onNodeWithTag("skillAndroid").performScrollTo().performClick()
        compose.onNodeWithTag("skillUiUx").performClick()
        compose.onNodeWithTag("proceed").assertIsNotEnabled()
    }

    private fun capture(name: String) {
        compose.waitForIdle()
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.waitForIdleSync()
        val destination = File(instrumentation.targetContext.getExternalFilesDir(null), "screenshots").apply { mkdirs() }
        val bitmap = requireNotNull(instrumentation.uiAutomation.takeScreenshot())
        File(destination, name).outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        bitmap.recycle()
    }
}
