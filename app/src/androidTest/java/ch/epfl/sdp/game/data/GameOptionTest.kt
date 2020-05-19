package ch.epfl.sdp.game.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameOptionTest {

    @Test
    fun defaultTest() {
        object : GameOption() {
            val distancesOn = false
        }
        Assert.assertTrue(true)
    }

}