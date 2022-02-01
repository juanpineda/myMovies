package com.juanpineda.mymovies.ui

import android.os.SystemClock
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.jakewharton.espresso.OkHttp3IdlingResource
import com.juanpineda.mymovies.R
import com.juanpineda.mymovies.data.server.TheMovieDb
import com.juanpineda.mymovies.ui.main.MainActivity
import com.juanpineda.mymovies.utils.fromJson
import okhttp3.mockwebserver.MockResponse
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.koin.test.KoinTest
import org.koin.test.get

class UiTest : KoinTest {

    private val mockWebServerRule = MockWebServerRule()

    companion object{
        @BeforeClass
        fun beforeClass() {
            InstrumentationRegistry.getInstrumentation().targetContext.deleteDatabase("movie-db")
        }
    }

    @get:Rule
    val testRule: RuleChain = RuleChain
        .outerRule(mockWebServerRule)
        .around(
            GrantPermissionRule.grant(
                "android.permission.ACCESS_COARSE_LOCATION"
            )
        )
        .around(ActivityScenarioRule(MainActivity::class.java))

    @Before
    fun setUp() {
        mockWebServerRule.server.enqueue(
            MockResponse().fromJson("popularmovies.json")
        )

        val resource = OkHttp3IdlingResource.create("OkHttp", get<TheMovieDb>().okHttpClient)
        IdlingRegistry.getInstance().register(resource)
    }

    @Test
    fun clickAMovieNavigatesToDetail() {
        SystemClock.sleep(1000)
        onView(withId(R.id.recycler)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                4,
                click()
            )
        )

        onView(withId(R.id.movieDetailToolbar))
            .check(matches(hasDescendant(withText("Captain Marvel"))))

    }
}