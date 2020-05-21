package ch.epfl.sdp.replay

import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.db.FakeAppDatabase
import ch.epfl.sdp.game.data.Faction
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@InternalCoroutinesApi
class FirebaseReplayRepositoryTest {
    @Before
    fun clearDB(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        FakeAppDatabase.instance(context).clearAllTables()
    }

    @Test
    fun getAllGamesDoesNotCrash() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val repo = FirebaseReplayRepository(ctx)
        var callbackCalled = false
        repo.getAllGames("https://www.youtube.com/watch?v=dQw4w9WgXcQ") {
            callbackCalled = true
        }

        for (x in 0..10) {
            if (callbackCalled) break
            Thread.sleep(100)
        }
        assertTrue(callbackCalled)
    }

    @Test
    fun getAllGamesWithEmptyUserIDShouldFallbackToLocalDB() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val repo = FirebaseReplayRepository(ctx)
        val localReplayStore = LocalReplayStore(ctx)
        var callbackCalled = false

        repo.getAllGames("") { initialReplayList ->
            assertEquals(0, initialReplayList.size)
            val replay = ReplayInfo("g4m31d", "s0m3 g4m3", 0, 0,"", Faction.PREY)
            localReplayStore.saveList(listOf(replay))

            repo.getAllGames("") { secondReplayList ->
                callbackCalled = true
                assertEquals(1, secondReplayList.size)
                assertEquals(replay, secondReplayList[0])
            }
        }

        for (x in 0..10) {
            if (callbackCalled) break
            Thread.sleep(100)
        }
        assertTrue(callbackCalled)
    }
}