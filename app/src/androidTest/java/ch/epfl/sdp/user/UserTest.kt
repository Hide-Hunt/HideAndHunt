package ch.epfl.sdp.user

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserTest {

    @Test
    fun defaultMatchesExpected() {
        val user = User("", 0)
        assertEquals("", user.name)
        assertEquals("0", user.uid)
    }

}