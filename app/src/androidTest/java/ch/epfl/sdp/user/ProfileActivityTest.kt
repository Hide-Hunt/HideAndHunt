package ch.epfl.sdp.user

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import androidx.core.view.get
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.matcher.IntentMatchers.toPackage
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import ch.epfl.sdp.R
import ch.epfl.sdp.authentication.LoginActivity
import ch.epfl.sdp.authentication.User
import ch.epfl.sdp.lobby.global.GlobalLobbyActivity
import kotlinx.coroutines.withContext
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.junit.*
import java.util.concurrent.CountDownLatch

class ProfileActivityTest {
    @get:Rule
    val activityRule = ActivityTestRule(ProfileActivity::class.java)
    @get:Rule
    var writeRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @get:Rule
    val intentsTestRule = IntentsTestRule(ProfileActivity::class.java)


    /*@After
    fun clean() {
        Intents.release()
    }

    @Before
    fun setup() {
        Intents.init()
    }*/
    @Test
    fun clickingOnImageViewOpensGallery() {
        launchActivity<ProfileActivity>()
        onView(withId(R.id.profilePictureView)).perform(click())
        intended(toPackage(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString()))
    }

    @Test
    fun createActivityWithNoProfilePicture() {
        User.profilePic = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        User.pseudo = "pseudo"
        User.connected = true
        val resultData = Intent()
        val whiteBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        resultData.putExtra("profilePic", whiteBitmap)
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
        //launchActivity<ProfileActivity>()

        intending(toPackage(MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString())).respondWith(result)
        onView(withId(R.id.profilePictureView)).perform(click())
        onView(withId(R.id.okButton)).perform(click())
        Assert.assertNotNull(activityRule.activity.getAlertDialog())
        Assert.assertTrue(activityRule.activity.getAlertDialog()!!.isShowing)

        onView(withText("OK")).perform(click())

        Assert.assertEquals(whiteBitmap, User.profilePic)
        Assert.assertTrue(activityRule.activity.isFinishing)
    }

    @Test
    fun createActivityWithProfilePicture() {
        val whiteBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        User.profilePic = whiteBitmap
        User.pseudo = "pseudo"
        User.connected = true
        launchActivity<ProfileActivity>()
        onView(withId(R.id.okButton)).perform(click())
        Assert.assertTrue(activityRule.activity.isFinishing)
        Assert.assertNotNull(User.profilePic)
        Assert.assertEquals(whiteBitmap, User.profilePic)
        Assert.assertEquals("pseudo", User.pseudo)
    }

    @Test
    fun onErrorShowsDialog() {
        activityRule.activity.onError()
        Assert.assertNotNull(activityRule.activity.getAlertDialog())
        Assert.assertTrue(activityRule.activity.getAlertDialog()!!.isShowing)
    }
}