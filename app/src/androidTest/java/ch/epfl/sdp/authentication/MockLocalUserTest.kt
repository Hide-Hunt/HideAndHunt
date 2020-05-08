package ch.epfl.sdp.authentication

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4::class)
class MockLocalUserTest {
    private val callbackLatch = CountDownLatch(1)
    private var correctCallback = false

    @Before
    fun forceDisconnect() {
        LocalUser.connected = false
    }

    private fun success() {
        callbackLatch.countDown()
        correctCallback = true
    }

    private fun error() {
        callbackLatch.countDown()
        correctCallback = false
    }

    @Test
    fun canConnectAndDisconnectAllBasicMockUsers() {
        correctCallback = false
        val mock = MockUserConnector()
        for (i in 0..5) {
            mock.connect("test$i@test.com", "password$i", {success()}, {error()})
            callbackLatch.await()
            Assert.assertTrue(correctCallback)
            Assert.assertEquals("test$i@test.com", LocalUser.email)
            Assert.assertEquals(i.toString(), LocalUser.uid)
            Assert.assertTrue(LocalUser.connected)
            mock.disconnect()
            Assert.assertFalse(LocalUser.connected)
        }
    }

    @Test
    fun cantConnectIfAlreadyConnected() {
        val mock = MockUserConnector()
        mock.connect("test0@test.com", "password0", {success()}, {error()})
        callbackLatch.await()
        Assert.assertTrue(correctCallback)
        Assert.assertTrue(LocalUser.connected)
        mock.connect("test0@test.com", "password0", {success()}, {error()})
        callbackLatch.await()
        Assert.assertFalse(correctCallback)
        Assert.assertTrue(LocalUser.connected)
    }

    @Test
    fun connectionWithWrongEmailFails() {
        val mock = MockUserConnector()
        mock.connect("fewfwef@test.com", "password0", {success()}, {error()})
        callbackLatch.await()
        Assert.assertFalse(correctCallback)
        Assert.assertFalse(LocalUser.connected)
    }

    @Test
    fun connectionWithWrongPasswordFails() {
        val mock = MockUserConnector()
        mock.connect("test0@test.com", "its2AMrightnow", {success()}, {error()})
        callbackLatch.await()
        Assert.assertFalse(correctCallback)
        Assert.assertFalse(LocalUser.connected)
    }

    @Test
    fun canAddUserThenConnectToIt() {
        val mock = MockUserConnector()
        mock.register("testing", "C-C-C-CANDEAAAA", "pseudo", {success()}, {error()})
        callbackLatch.await()
        Assert.assertTrue(correctCallback)
        Assert.assertEquals("testing", LocalUser.email)
        Assert.assertEquals("pseudo", LocalUser.pseudo)
        Assert.assertTrue(LocalUser.connected)
        mock.disconnect()
        Assert.assertFalse(LocalUser.connected)
        mock.connect("testing", "C-C-C-CANDEAAAA", {success()}, {error()})
        callbackLatch.await()
        Assert.assertTrue(correctCallback)
        Assert.assertEquals("testing", LocalUser.email)
        Assert.assertEquals("pseudo", LocalUser.pseudo)
        Assert.assertTrue(LocalUser.connected)
    }

    @Test
    fun addingAlreadyExistingUserFails() {
        val mock = MockUserConnector()
        mock.register("test0@test.com", "Covid-19", "pseudo", {success()}, {error()})
        callbackLatch.await()
        Assert.assertFalse(correctCallback)
        Assert.assertFalse(LocalUser.connected)
    }
}
