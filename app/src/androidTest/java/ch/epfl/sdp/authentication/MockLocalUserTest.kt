package ch.epfl.sdp.authentication

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MockLocalUserTest {
    @Before
    fun forceDisconnect() {
        LocalUser.connected = false
    }

    @Test
    fun canConnectAndDisconnectAllBasicMockUsers() {
        val mock = MockUserConnector()
        for (i in 0..5) {
            Assert.assertTrue(mock.connect("test$i@test.com", "password$i"))
            Assert.assertEquals("test$i@test.com", LocalUser.username)
            Assert.assertEquals(i.toString(), LocalUser.uid)
            Assert.assertTrue(LocalUser.connected)
            Assert.assertTrue(mock.disconnect())
            Assert.assertFalse(LocalUser.connected)
        }
    }

    @Test
    fun cantConnectIfAlreadyConnected() {
        val mock = MockUserConnector()
        Assert.assertTrue(mock.connect("test0@test.com", "password0"))
        Assert.assertTrue(LocalUser.connected)
        Assert.assertFalse(mock.connect("test0@test.com", "password0"))
        Assert.assertTrue(LocalUser.connected)
    }

    @Test
    fun connectionWithWrongEmailFails() {
        val mock = MockUserConnector()
        Assert.assertFalse(mock.connect("fewfwef@test.com", "password0"))
        Assert.assertFalse(LocalUser.connected)
    }

    @Test
    fun connectionWithWrongPasswordFails() {
        val mock = MockUserConnector()
        Assert.assertFalse(mock.connect("test0@test.com", "its2AMrightnow"))
        Assert.assertFalse(LocalUser.connected)
    }

    @Test
    fun canAddUserThenConnectToIt() {
        val mock = MockUserConnector()
        Assert.assertTrue(mock.register("testing", "C-C-C-CANDEAAAA"))
        Assert.assertEquals("testing", LocalUser.username)
        Assert.assertTrue(LocalUser.connected)
        Assert.assertTrue(mock.disconnect())
        Assert.assertFalse(LocalUser.connected)
        Assert.assertTrue(mock.connect("testing", "C-C-C-CANDEAAAA"))
        Assert.assertEquals("testing", LocalUser.username)
        Assert.assertTrue(LocalUser.connected)
    }

    @Test
    fun addingAlreadyExistingUserFails() {
        val mock = MockUserConnector()
        Assert.assertFalse(mock.register("test0@test.com", "Covid-19"))
        Assert.assertFalse(LocalUser.connected)
    }
}
