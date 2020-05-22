package ch.epfl.sdp.dagger.modules

import ch.epfl.sdp.replay.FirebaseReplayDownloader
import org.junit.Assert.assertTrue
import org.junit.Test

class ReplayModuleTest {
    @Test
    fun testProvidesFirebaseDownloader() {
        val repo = ReplayModule()
        assertTrue(repo.providesFirebaseDownloader() is FirebaseReplayDownloader)
    }
}