package kr.yangbob.unitanduitest

import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class EspressoTest {
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun displayTest() {
        onView(withId(R.id.btn_input)).check(matches(isDisplayed()))
        onView(withId(R.id.et_input)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_input)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_description)).check(matches(isDisplayed()))
    }

    @Test
    fun inputTest(){
        val et = onView(withId(R.id.et_input))
        val tv = onView(withId(R.id.tv_input))
        tv.check(matches(withText("입력값")))
        et.check(matches(withText("")))
        onView(withId(R.id.et_input)).perform(typeText("test text"))
        closeSoftKeyboard()
        onView(withId(R.id.btn_input)).perform(click())
        tv.check(matches(withText("test text")))
        et.check(matches(withText("")))
    }
}