package ch.epfl.sdp.replay

import ch.epfl.sdp.TestWait.wait
import org.junit.Assert.*
import org.junit.Test
import java.io.File

class FirebaseReplayDownloaderTest {
    @Test
    fun downloadingInvalidGameIDShouldFail() {
        val replayDL = FirebaseReplayDownloader()
        val tmpFile = File.createTempFile("test", null)

        var callbackCalled = false

        replayDL.download("", tmpFile, {}, {callbackCalled = true})
        wait(1000, {callbackCalled})

        assertTrue(callbackCalled)
    }
}