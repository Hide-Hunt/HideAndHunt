package ch.epfl.sdp.replay

import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.db.FakeAppDatabase
import ch.epfl.sdp.game.data.Faction
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.After
import org.junit.Assert.*
import org.junit.BeforeClass
import org.junit.Test

@InternalCoroutinesApi
class FirebaseReplayRepositoryTest {
    @BeforeClass
    @After
    fun clearDB(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        FakeAppDatabase.instance(context).clearAllTables()
    }

    @Test
    fun getAllGamesDoesNotCrash() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val repo = FirebaseReplayRepository(ctx)
        repo.getAllGames("https://www.youtube.com/watch?v=dQw4w9WgXcQ") { Unit }
    }

    @Test
    fun getAllGamesWithEmptyUserIDShouldFallbackToLocalDB() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val repo = FirebaseReplayRepository(ctx)
        val localReplayStore = LocalReplayStore(ctx)

        repo.getAllGames("") { initialReplayList ->
            assertTrue(initialReplayList.isEmpty())
            val replay = ReplayInfo("g4m31d", "s0m3 g4m3", 0, 0,"", Faction.PREY)
            localReplayStore.saveList(listOf(replay))

            repo.getAllGames("") { secondReplayList ->
                assertEquals(1, secondReplayList.size)
                assertEquals(replay, secondReplayList[1])
            }
        }
    }
}