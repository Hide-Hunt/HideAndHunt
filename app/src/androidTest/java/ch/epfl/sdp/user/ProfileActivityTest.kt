package ch.epfl.sdp.user

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import ch.epfl.sdp.R
import ch.epfl.sdp.authentication.LocalUser
import org.junit.*

class ProfileActivityTest {
    @get:Rule
    val intentsTestRule = IntentsTestRule(ProfileActivity::class.java, false, false)

    @After
    fun clean() {
        Intents.release()
    }

    @Before
    fun setup() {
        Intents.init()
    }

    @Test
    fun clickingOnImageViewOpensGallery() {
        launchActivity<ProfileActivity>()
        onView(withId(R.id.profilePictureView)).perform(click())
        intended(IntentMatchers.hasAction("android.intent.action.CHOOSER"))
    }

    @Test
    fun pseudoTextShowsCurrentPseudo() {
        LocalUser.profilePic = null
        LocalUser.pseudo = "pseudo"
        LocalUser.connected = true
        launchActivity<ProfileActivity>()
        onView(withId(R.id.pseudoText)).check(matches(withText("pseudo")))
    }

    @Test
    fun createActivityWithProfilePicture() {
        val whiteBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        LocalUser.profilePic = whiteBitmap
        LocalUser.pseudo = "wrongPseudo"
        LocalUser.connected = true
        launchActivity<ProfileActivity>()
        onView(withId(R.id.pseudoText)).perform(ViewActions.clearText())
        onView(withId(R.id.pseudoText)).perform(ViewActions.typeText("correctPseudo"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.okButton)).perform(click())

        Assert.assertTrue(LocalUser.profilePic!!.sameAs(whiteBitmap))
        Assert.assertEquals("correctPseudo", LocalUser.pseudo)
    }

    @Test
    fun createActivityWithNoProfilePicture() {
        LocalUser.profilePic = null
        LocalUser.pseudo = "wrongPseudo"
        LocalUser.connected = true
        val resultData = Intent()
        val path = Uri.parse("android.resource://ch.epfl.sdp/" + R.drawable.star_icon)
        resultData.putExtra("picturePath", path)
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
        launchActivity<ProfileActivity>()
        val whiteBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)

        intending(IntentMatchers.hasAction("android.intent.action.CHOOSER")).respondWith(result)
        onView(withId(R.id.profilePictureView)).perform(click())
        LocalUser.profilePic = whiteBitmap
        onView(withId(R.id.pseudoText)).perform(ViewActions.clearText())
        onView(withId(R.id.pseudoText)).perform(ViewActions.typeText("correctPseudo"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.okButton)).perform(click())

        Assert.assertFalse(LocalUser.profilePic!!.sameAs(whiteBitmap))
        Assert.assertEquals("correctPseudo", LocalUser.pseudo)
    }

    @Test
    fun onErrorShowsDialog() {
        LocalUser.profilePic = null
        LocalUser.pseudo = "wrongPseudo"
        LocalUser.connected = true
        launchActivity<ProfileActivity>()
        onView(withId(R.id.pseudoText)).perform(ViewActions.clearText())
        onView(withId(R.id.pseudoText)).perform(ViewActions.typeText("REQUESTING_ERROR"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.okButton)).perform(click())
        onView(withText("Error")).check(matches(isDisplayed()));
    }
}