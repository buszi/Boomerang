package io.buszi.boomerang

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI tests for the Fragment implementation of Boomerang.
 * Tests the four key scenarios:
 * 1. Basic navigation result flow
 * 2. Result persistence (recreate activity when in between screen)
 * 3. Dropping value
 * 4. Proxy value in-between
 */
@RunWith(AndroidJUnit4::class)
class FragmentBoomerangTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(FullFragmentPreviewActivity::class.java)

    /**
     * Test Case 1: Basic navigation result flow
     * 
     * Steps:
     * 1. Navigate from FragmentA to FragmentB
     * 2. Navigate from FragmentB to FragmentC
     * 3. Store a result in FragmentC and navigate back
     * 4. Verify that the result is displayed in FragmentA
     */
    @Test
    fun testBasicNavigationResultFlow() {
        // Verify we're on FragmentA
        onView(withText("No result yet")).check(matches(isDisplayed()))

        // Navigate to FragmentB
        onView(withId(R.id.navigate_button)).perform(click())

        // Navigate to FragmentC
        onView(withId(R.id.navigate_button)).perform(click())

        // Store result and navigate back to home screen
        onView(withId(R.id.navigate_back_button)).perform(click())
        Espresso.pressBack()

        // Verify we're back on FragmentA and the result is displayed
        onView(withText("Result from result screen")).check(matches(isDisplayed()))
    }

    /**
     * Test Case 2: Result persistence (recreate activity when in between screen)
     * 
     * Steps:
     * 1. Navigate from FragmentA to FragmentB
     * 2. Navigate from FragmentB to FragmentC
     * 3. Store a result in FragmentC and navigate back to FragmentB
     * 4. Recreate the activity
     * 5. Navigate back to FragmentA
     * 6. Verify that the result is still displayed in FragmentA
     */
    @Test
    fun testResultPersistence() {
        // Navigate to FragmentB
        onView(withId(R.id.navigate_button)).perform(click())

        // Navigate to FragmentC
        onView(withId(R.id.navigate_button)).perform(click())

        // Store result and navigate back to FragmentB
        onView(withId(R.id.navigate_back_button)).perform(click())

        // Recreate the activity
        activityRule.scenario.recreate()

        // Navigate back to FragmentA
        Espresso.pressBack()

        // Verify the result is still displayed in FragmentA
        onView(withText("Result from result screen")).check(matches(isDisplayed()))
    }

    /**
     * Test Case 3: Dropping value
     * 
     * Steps:
     * 1. Navigate from FragmentA to FragmentB
     * 2. Navigate from FragmentB to FragmentC
     * 3. Store a result in FragmentC and navigate back to FragmentB
     * 4. Drop the value in FragmentB
     * 5. Navigate back to FragmentA
     * 6. Verify that no result is displayed in FragmentA
     */
    @Test
    fun testDroppingValue() {
        // Navigate to FragmentB
        onView(withId(R.id.navigate_button)).perform(click())

        // Navigate to FragmentC
        onView(withId(R.id.navigate_button)).perform(click())

        // Store result and navigate back to FragmentB
        onView(withId(R.id.navigate_back_button)).perform(click())

        // Drop the value
        onView(withId(R.id.drop_value_button)).perform(click())

        // Navigate back to FragmentA
        Espresso.pressBack()

        // Verify no result is displayed in FragmentA
        onView(withText("No result yet")).check(matches(isDisplayed()))
    }

    /**
     * Test Case 4: Proxy value in-between
     * 
     * Steps:
     * 1. Navigate from FragmentA to FragmentB
     * 2. Navigate from FragmentB to FragmentC
     * 3. Store a result in FragmentC and navigate back to FragmentB
     * 4. Consume the value in FragmentB
     * 5. Verify that the proxied value is displayed in FragmentB
     * 6. Navigate back to FragmentA
     * 7. Verify that no result is displayed in FragmentA
     */
    @Test
    fun testProxyValueInBetween() {
        // Navigate to FragmentB
        onView(withId(R.id.navigate_button)).perform(click())

        // Navigate to FragmentC
        onView(withId(R.id.navigate_button)).perform(click())

        // Store result and navigate back to FragmentB
        onView(withId(R.id.navigate_back_button)).perform(click())

        // Consume the value
        onView(withId(R.id.consume_value_button)).perform(click())

        // Verify the proxied value is displayed in FragmentB
        onView(withText("Proxied value: Result from result screen")).check(matches(isDisplayed()))

        // Navigate back to FragmentA
        Espresso.pressBack()

        // Verify no result is displayed in FragmentA
        onView(withText("No result yet")).check(matches(isDisplayed()))
    }
}
