package ch.epfl.sdp.user

import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import ch.epfl.sdp.R
import ch.epfl.sdp.authentication.LoginActivity
import ch.epfl.sdp.authentication.User
import ch.epfl.sdp.lobby.global.GlobalLobbyActivity
import org.junit.*
import java.util.concurrent.CountDownLatch

class ProfileActivityTest {
    @get:Rule
    val activityRule = ActivityTestRule(ProfileActivity::class.java)
    @get:Rule
    var writeRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    @After
    fun clean() {
        Intents.release()
    }

    @Before
    fun setup() {
        Intents.init()
    }

    @Test
    fun createActivityWithNoProfilePicture() {
        User.profilePic = null
        User.pseudo = ""
        launchActivity<ProfileActivity>()
        Espresso.onView(withId(R.id.userPasswordLogin)).perform(ViewActions.typeText("pseudo"), ViewActions.closeSoftKeyboard())
        Thread.sleep(1000)
        Espresso.onView(withId(R.id.okButton)).perform(click())
        Espresso.onView(withText("Successfully uploaded profile pic")).check(matches(isDisplayed()))
        //Espresso.onView(withText("OK")).perform(click())
        Assert.assertNotNull(User.profilePic)
        Assert.assertEquals("pseudo", User.pseudo)
    }

    @Test
    fun createActivityWithProfilePicture() {
        val whiteBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        User.profilePic = whiteBitmap
        User.pseudo = "pseudo"
        User.connected = true
        launchActivity<ProfileActivity>()
        Espresso.onView(withId(R.id.okButton)).perform(click())
        Assert.assertNotNull(User.profilePic)
        Assert.assertEquals(whiteBitmap, User.profilePic)
        Assert.assertEquals("pseudo", User.pseudo)
    }

    @Test
    fun onErrorShowsDialog() {
        activityRule.activity.onError()
        Espresso.onView(withText("Error")).check(matches(isDisplayed()))
    }
}