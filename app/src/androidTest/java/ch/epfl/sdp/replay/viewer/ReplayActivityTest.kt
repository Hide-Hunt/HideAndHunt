package ch.epfl.sdp.replay.viewer

import android.content.Intent
import android.util.Base64
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import ch.epfl.sdp.R
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.File

class ReplayActivityTest {

    private val activityIntent = Intent()
    init {
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activityIntent.putExtra("replay_path", "4.game")
    }

    private val invalidPathActivityIntent = Intent()
    private val invalidFilename = "inexisting"
    init {
        invalidPathActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        invalidPathActivityIntent.putExtra("replay_path", invalidFilename)
    }

    private val emptyIntent = Intent()
    init {
        emptyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    private val badFormatExampleFile = "CCoSC1"

    private val exampleFile = "CCoSC1J1biB0byBTYXQhGAEl2Ih2Xi0niXZeUgBSBAgBEAFaGw3aiHZeWhQSEgnkaea2akJHQBGI" +
                                    "9NvXgUMaQFodDeCIdl5aFggBEhIJ5GnmtmpCR0ARiPTb14FDGkBaGw3giHZeWhQSEgl88rBQa0JH" +
                                    "QBEEgFAEXkMaQFodDeKIdl5aFggBEhIJfPKwUGtCR0ARBIBQBF5DGkBaGw3iiHZeWhQSEgkTe3vq" +
                                    "a0JHQBF+KKv8RUMaQFodDeWIdl5aFggBEhIJE3t76mtCR0ARfiir/EVDGkBaGw3liHZeWhQSEgke" +
                                    "waBfbkJHQBGvTyZbSkMaQFodDeeIdl5aFggBEhIJHsGgX25CR0ARr08mW0pDGkBaGw3niHZeWhQS" +
                                    "EgnaNTR4ckJHQBGc2cGbSEMaQFodDemIdl5aFggBEhIJ2jU0eHJCR0ARnNnBm0hDGkBaGw3piHZe" +
                                    "WhQSEglEw08JdUJHQBEzbf/KSkMaQFodDeuIdl5aFggBEhIJRMNPCXVCR0ARM23/ykpDGkBaGw3r" +
                                    "iHZeWhQSEgl8D5ccd0JHQBGkFHR7SUMaQFodDe2Idl5aFggBEhIJfA+XHHdCR0ARpBR0e0lDGkBa" +
                                    "Gw3tiHZeWhQSEgnmnLKteUJHQBGc2cGbSEMaQFodDe+Idl5aFggBEhIJ5pyyrXlCR0ARnNnBm0hD" +
                                    "GkBaGw3viHZeWhQSEglCv9wUfEJHQBGkFHR7SUMaQFodDfGIdl5aFggBEhIJQr/cFHxCR0ARpBR0" +
                                    "e0lDGkBaGw3xiHZeWhQSEgk5mhBgfkJHQBFilHopT0MaQFodDfOIdl5aFggBEhIJOZoQYH5CR0AR" +
                                    "YpR6KU9DGkBaGw3ziHZeWhQSEglVQLRpf0JHQBFGHTAiZEMaQFodDfaIdl5aFggBEhIJVUC0aX9C" +
                                    "R0ARRh0wImRDGkBaGw32iHZeWhQSEglPG+i0gUJHQBEN2OivakMaQFodDfeIdl5aFggBEhIJTxvo" +
                                    "tIFCR0ARDdjor2pDGkBaGw33iHZeWhQSEgk1iyrWg0JHQBEEgFAEXkMaQFodDfmIdl5aFggBEhIJ" +
                                    "NYsq1oNCR0ARBIBQBF5DGkBaGw35iHZeWhQSEgn5L/04h0JHQBHxCexEXEMaQFodDfuIdl5aFggB" +
                                    "EhIJ+S/9OIdCR0AR8QnsRFxDGkBaGw37iHZeWhQSEgl3KAr0iUJHQBHLHSPGWEMaQFodDf2Idl5a" +
                                    "FggBEhIJdygK9IlCR0ARyx0jxlhDGkBaGw39iHZeWhQSEgmU2SCTjEJHQBEVMIFbd0MaQFodDQCJ" +
                                    "dl5aFggBEhIJlNkgk4xCR0ARFTCBW3dDGkBaGw0AiXZeWhQSEgkZAm2YjkJHQBGkpfJ2hEMaQFod" +
                                    "DQSJdl5aFggBEhIJGQJtmI5CR0ARpKXydoRDGkBaGw0EiXZeWhQSEglblNkgk0JHQBF+8/WPmEMa" +
                                    "QFodDQaJdl5aFggBEhIJW5TZIJNCR0ARfvP1j5hDGkBaGw0GiXZeWhQSEgmCgKKflkJHQBGk374O" +
                                    "nEMaQFodDQiJdl5aFggBEhIJgoCin5ZCR0ARpN++DpxDGkBaGw0IiXZeWhQSEgm611xImkJHQBEz" +
                                    "OEpenUMaQFodDQqJdl5aFggBEhIJutdcSJpCR0ARMzhKXp1DGkBaGw0KiXZeWhQSEgnCEg8om0JH" +
                                    "QBECaIFjvEMaQFodDQyJdl5aFggBEhIJwhIPKJtCR0ARAmiBY7xDGkBaGw0MiXZeWhQSEgkbKsb5" +
                                    "m0JHQBHLXAaJ2kMaQFodDQ+Jdl5aFggBEhIJGyrG+ZtCR0ARy1wGidpDGkBaGw0PiXZeWhQSEglZ" +
                                    "pkxXnUJHQBGRF78W4UMaQFodDRGJdl5aFggBEhIJWaZMV51CR0ARkRe/FuFDGkBaGw0RiXZeWhQS" +
                                    "EgmyvQMpnkJHQBGajD2O+UMaQFodDROJdl5aFggBEhIJsr0DKZ5CR0ARmow9jvlDGkBaGw0TiXZe" +
                                    "WhQSEgmR/QYAoUJHQBEo5cjd+kMaQFodDRaJdl5aFggBEhIJkf0GAKFCR0ARKOXI3fpDGkBaGw0W" +
                                    "iXZeWhQSEgnReY1dokJHQBGtPG7lEkQaQFodDRiJdl5aFggBEhIJ0XmNXaJCR0ARrTxu5RJEGkBa" +
                                    "Gw0YiXZeWhQSEglVl2bNokJHQBEeWHvFQEQaQFodDRuJdl5aFggBEhIJVZdmzaJCR0ARHlh7xUBE" +
                                    "GkBaGw0biXZeWhQSEgnzT3CxokJHQBHcaABvgUQaQFodDR2Jdl5aFggBEhIJ809wsaJCR0AR3GgA" +
                                    "b4FEGkBaGw0diXZeWhQSEgkWJlMFo0JHQBEeC/e38UQaQFodDSCJdl5aFggBEhIJFiZTBaNCR0AR" +
                                    "Hgv3t/FEGkBaGw0giXZeWhQSEgnReY1dokJHQBG+amXCL0UaQFodDSKJdl5aFggBEhIJ0XmNXaJC" +
                                    "R0ARvmplwi9FGkBaGw0iiXZeWhQSEglvMpdBokJHQBGYDxs/Z0UaQFodDSWJdl5aFggBEhIJbzKX" +
                                    "QaJCR0ARmA8bP2dFGkBaGw0liXZeWhQSEgl3V2P2n0JHQBHRji5JeEUaQFodDSaJdl5aFggBEhIJ" +
                                    "d1dj9p9CR0AR0Y4uSXhFGkBaCQ0niXZeUgIIAQ=="

    @get:Rule
    val activityRule = ActivityTestRule(ReplayActivity::class.java, false, false)
    @get:Rule
    var grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    @get:Rule
    var grantPermissionRule1: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.READ_EXTERNAL_STORAGE)

    @After
    fun clean() {
        Intents.release()
    }

    @Before
    fun setup() {
        Intents.init()
    }

    private fun checkFolderExists() {
        File(activityRule.activity.filesDir.absolutePath + "/replays").let {
            if (!it.exists()) {
                it.mkdir()
            }
        }
    }

    private fun createFile(filename: String, fileContent: String) {
        activityRule.launchActivity(emptyIntent)
        val prepActivity = activityRule.activity
        checkFolderExists()
        val file = File(prepActivity.filesDir.absolutePath + "/replays/$filename")
        file.writeBytes(Base64.decode(fileContent, Base64.DEFAULT))
        activityRule.finishActivity()
    }

    @Test
    fun testNoFilePathProvided(){
        activityRule.launchActivity(emptyIntent)
        Espresso.onView(ViewMatchers.withId(R.id.errorDetails)).check(ViewAssertions.matches(withText(R.string.missing_replay_path_parameter)))
    }

    @Test
    fun testInvalidFilePathProvided(){
        activityRule.launchActivity(invalidPathActivityIntent)
        val file = activityRule.activity.filesDir.absolutePath + "/replays/" + invalidFilename
        Espresso.onView(ViewMatchers.withId(R.id.errorDetails)).check(ViewAssertions.matches(withText("File not found $file")))
    }

    @Test
    fun testValidFilePathProvided(){
        createFile("4.game", exampleFile)
        activityRule.launchActivity(activityIntent)
        Espresso.onView(ViewMatchers.withId(R.id.errorDetails)).check(ViewAssertions.matches(withText("")))
    }

    @Test
    fun testValidFileWrongFormatPathProvided(){
        createFile("4.game", badFormatExampleFile)
        activityRule.launchActivity(activityIntent)
        Espresso.onView(ViewMatchers.withId(R.id.errorDetails)).check(ViewAssertions.matches(withText("Replay file format error")))
    }
}