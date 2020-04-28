package ch.epfl.sdp.authentication

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4::class)
class MockUserTest {
    val callbackLatch = CountDownLatch(1)
    var correctCallback = false

    @Before
    fun forceDisconnect() {
        User.connected = false
    }

    fun success() {
        callbackLatch.countDown()
        correctCallback = true
    }

    fun error() {
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
            Assert.assertEquals("test$i@test.com", User.email)
            Assert.assertEquals(i.toString(), User.uid)
            Assert.assertTrue(User.connected)
            mock.disconnect()
            Assert.assertFalse(User.connected)
        }
    }

    @Test
    fun cantConnectIfAlreadyConnected() {
        val mock = MockUserConnector()
        mock.connect("test0@test.com", "password0", {success()}, {error()})
        callbackLatch.await()
        Assert.assertTrue(correctCallback)
        Assert.assertTrue(User.connected)
        mock.connect("test0@test.com", "password0", {success()}, {error()})
        callbackLatch.await()
        Assert.assertFalse(correctCallback)
        Assert.assertTrue(User.connected)
    }

    @Test
    fun connectionWithWrongEmailFails() {
        val mock = MockUserConnector()
        mock.connect("fewfwef@test.com", "password0", {success()}, {error()})
        callbackLatch.await()
        Assert.assertFalse(correctCallback)
        Assert.assertFalse(User.connected)
    }

    @Test
    fun connectionWithWrongPasswordFails() {
        val mock = MockUserConnector()
        mock.connect("test0@test.com", "its2AMrightnow", {success()}, {error()})
        callbackLatch.await()
        Assert.assertFalse(correctCallback)
        Assert.assertFalse(User.connected)
    }

    @Test
    fun canAddUserThenConnectToIt() {
        val mock = MockUserConnector()
        mock.register("testing", "C-C-C-CANDEAAAA", "pseudo", {success()}, {error()})
        callbackLatch.await()
        Assert.assertTrue(correctCallback)
        Assert.assertEquals("testing", User.email)
        Assert.assertEquals("pseudo", User.pseudo)
        Assert.assertTrue(User.connected)
        mock.disconnect()
        Assert.assertFalse(User.connected)
        mock.connect("testing", "C-C-C-CANDEAAAA", {success()}, {error()})
        callbackLatch.await()
        Assert.assertTrue(correctCallback)
        Assert.assertEquals("testing", User.email)
        Assert.assertEquals("pseudo", User.pseudo)
        Assert.assertTrue(User.connected)
    }

    @Test
    fun addingAlreadyExistingUserFails() {
        val mock = MockUserConnector()
        mock.register("test0@test.com", "Covid-19", "pseudo", {success()}, {error()})
        callbackLatch.await()
        Assert.assertFalse(correctCallback)
        Assert.assertFalse(User.connected)
    }
}
