package com.bignerdranch.android.beatbox

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//this is an Android instrumentation test
// that might want to work with activities and other Android runtime toys.
@RunWith(AndroidJUnit4::class)
class MainActivityTest{

    //the @get:Rule annotation on activityRule signals to JUnit that it should
    // fire up an instance of MainActivity before running each test.
    @get: Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun showsFirstFileName(){
        onView(withText("65_cjipe"))
            .check(matches(isDisplayed()))
    }
}