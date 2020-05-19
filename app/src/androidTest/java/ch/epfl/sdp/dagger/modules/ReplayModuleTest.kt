package ch.epfl.sdp.dagger.modules

import ch.epfl.sdp.replay.FakeReplayDownloader
import org.junit.Assert.assertTrue
import org.junit.Test

class ReplayModuleTest {

    @Test
    fun testProvidesFirebaseDownloader() {
        val replay = FakeReplayModule()
        val gottenDownloader = replay.providesFirebaseDownloader()
        assertTrue(gottenDownloader is FakeReplayDownloader)
    }

}