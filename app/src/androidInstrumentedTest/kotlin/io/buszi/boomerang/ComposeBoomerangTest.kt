package io.buszi.boomerang

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI tests for the Compose implementation of Boomerang.
 * Tests the five key scenarios:
 * 1. Basic navigation result flow
 * 2. Result persistence (recreate activity when in between screen)
 * 3. Dropping value
 * 4. Proxy value in-between
 * 5. Event handling
 */
@RunWith(AndroidJUnit4::class)
class ComposeBoomerangTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<FullComposePreviewActivity>()

    /**
     * Test Case 1: Basic navigation result flow
     * 
     * Steps:
     * 1. Navigate from Home to Intermediate screen
     * 2. Navigate from Intermediate to Result screen
     * 3. Store a result in Result screen and navigate back
     * 4. Verify that the result is displayed in Home screen
     */
    @Test
    fun testBasicNavigationResultFlow() {
        // Verify we're on Home screen
        composeTestRule.onNodeWithText("Home Screen").assertIsDisplayed()
        composeTestRule.onNodeWithText("Current result: No result yet").assertIsDisplayed()

        // Navigate to Intermediate screen
        composeTestRule.onNodeWithText("Navigate to Intermediate Screen").performClick()

        // Navigate to Result screen
        composeTestRule.onNodeWithText("Continue to Result Screen").performClick()

        // Store result and navigate back to home screen
        composeTestRule.onNodeWithText("Store Result and Return").performClick()
        Espresso.pressBack()

        // Verify we're back on Home screen and the result is displayed
        composeTestRule.onNodeWithText("Home Screen").assertIsDisplayed()
        composeTestRule.onNodeWithText("Current result: Result from result screen").assertIsDisplayed()
    }

    /**
     * Test Case 2: Result persistence (recreate activity when in between screen)
     * 
     * Steps:
     * 1. Navigate from Home to Intermediate screen
     * 2. Navigate from Intermediate to Result screen
     * 3. Store a result in Result screen and navigate back to Intermediate screen
     * 4. Recreate the activity
     * 5. Navigate back to Home screen
     * 6. Verify that the result is still displayed in Home screen
     */
    @Test
    fun testResultPersistence() {
        // Navigate to Intermediate screen
        composeTestRule.onNodeWithText("Navigate to Intermediate Screen").performClick()

        // Navigate to Result screen
        composeTestRule.onNodeWithText("Continue to Result Screen").performClick()

        // Store result and navigate back to Intermediate screen
        composeTestRule.onNodeWithText("Store Result and Return").performClick()

        // Verify we're on Intermediate screen
        composeTestRule.onNodeWithText("Intermediate Screen").assertIsDisplayed()

        // Recreate the activity
        composeTestRule.activityRule.scenario.recreate()

        // Navigate back to Home screen
        Espresso.pressBack()

        // Verify the result is still displayed in Home screen
        composeTestRule.onNodeWithText("Current result: Result from result screen").assertIsDisplayed()
    }

    /**
     * Test Case 3: Dropping value
     * 
     * Steps:
     * 1. Navigate from Home to Intermediate screen
     * 2. Navigate from Intermediate to Result screen
     * 3. Store a result in Result screen and navigate back to Intermediate screen
     * 4. Drop the value in Intermediate screen
     * 5. Navigate back to Home screen
     * 6. Verify that no result is displayed in Home screen
     */
    @Test
    fun testDroppingValue() {
        // Navigate to Intermediate screen
        composeTestRule.onNodeWithText("Navigate to Intermediate Screen").performClick()

        // Navigate to Result screen
        composeTestRule.onNodeWithText("Continue to Result Screen").performClick()

        // Store result and navigate back to Intermediate screen
        composeTestRule.onNodeWithText("Store Result and Return").performClick()

        // Drop the value
        composeTestRule.onNodeWithText("Drop Home Result").performClick()

        // Navigate back to Home screen
        Espresso.pressBack()

        // Verify no result is displayed in Home screen
        composeTestRule.onNodeWithText("Current result: No result yet").assertIsDisplayed()
    }

    /**
     * Test Case 4: Proxy value in-between
     * 
     * Steps:
     * 1. Navigate from Home to Intermediate screen
     * 2. Navigate from Intermediate to Result screen
     * 3. Store a result in Result screen and navigate back to Intermediate screen
     * 4. Consume the value in Intermediate screen
     * 5. Verify that the proxied value is displayed in Intermediate screen
     * 6. Navigate back to Home screen
     * 7. Verify that no result is displayed in Home screen
     */
    @Test
    fun testProxyValueInBetween() {
        // Navigate to Intermediate screen
        composeTestRule.onNodeWithText("Navigate to Intermediate Screen").performClick()

        // Navigate to Result screen
        composeTestRule.onNodeWithText("Continue to Result Screen").performClick()

        // Store result and navigate back to Intermediate screen
        composeTestRule.onNodeWithText("Store Result and Return").performClick()

        // Consume the value
        composeTestRule.onNodeWithText("Consume Value").performClick()

        // Verify the proxied value is displayed in Intermediate screen
        composeTestRule.onNodeWithText("Proxied value: Result from result screen").assertIsDisplayed()

        // Navigate back to Home screen
        Espresso.pressBack()

        // Verify no result is displayed in Home screen
        composeTestRule.onNodeWithText("Current result: No result yet").assertIsDisplayed()
    }

    /**
     * Test Case 5: Event handling
     * 
     * Steps:
     * 1. Verify initial event status is "No event received"
     * 2. Navigate from Home to Intermediate screen
     * 3. Navigate from Intermediate to Result screen
     * 4. Trigger an event in Result screen and navigate back
     * 5. Verify that the event status is updated to "Event received"
     * 6. Clear the event status
     * 7. Verify that the event status is reset to "No event received"
     */
    @Test
    fun testEventHandling() {
        // Verify we're on Home screen and initial event status
        composeTestRule.onNodeWithText("Home Screen").assertIsDisplayed()
        composeTestRule.onNodeWithText("Event status: No event received").assertIsDisplayed()

        // Navigate to Intermediate screen
        composeTestRule.onNodeWithText("Navigate to Intermediate Screen").performClick()

        // Navigate to Result screen
        composeTestRule.onNodeWithText("Continue to Result Screen").performClick()

        // Trigger event and navigate back
        composeTestRule.onNodeWithText("Trigger Event and Return").performClick()
        Espresso.pressBack()

        // Verify we're back on Home screen and the event status is updated
        composeTestRule.onNodeWithText("Home Screen").assertIsDisplayed()
        composeTestRule.onNodeWithText("Event status: Event received").assertIsDisplayed()

        // Clear the event status
        composeTestRule.onNodeWithText("Clear Event Status").performClick()

        // Verify the event status is reset
        composeTestRule.onNodeWithText("Event status: No event received").assertIsDisplayed()
    }
}
