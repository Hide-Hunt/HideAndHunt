package ch.epfl.sdp.replay

import androidx.test.platform.app.InstrumentationRegistry
import ch.epfl.sdp.db.FakeAppDatabase
import ch.epfl.sdp.game.data.Faction
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

@InternalCoroutinesApi
class LocalReplayStoreTest{
    @Before
    fun initDB(){

        val mockReplayList = listOf(
                ReplayInfo("0", "Game #0", 0, 2345, "", Faction.PREDATOR, true),
                ReplayInfo("1", "Game #1", 6753759194, 6753759194 + 675, "", Faction.PREDATOR, true),
                ReplayInfo("2", "Game #2", 964781131, 964781131 + 182, "", Faction.PREY, true),
                ReplayInfo("3", "Game #3", 1982211276, 1982211276 + 871, "", Faction.PREDATOR, true),
                ReplayInfo("4", "Game #4", 5893518155, 5893518155 + 139, "", Faction.PREY, true),
                ReplayInfo("5", "Game #5", 8505536244, 8505536244 + 549, "", Faction.PREY, true)
        )

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        for (ri in mockReplayList) {
            FakeAppDatabase.instance(context).replayDao().insert(ri)
        }
    }

    @BeforeClass
    @After
    fun clearDB(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        FakeAppDatabase.instance(context).clearAllTables()
    }

    @Test
    fun testGetList(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        var check = false
        LocalReplayStore(context).getList{l: List<ReplayInfo> -> check = l.size == 6}
        Thread.sleep(100)
        assert(check)
    }

    @Test
    fun testSaveList(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        var check = false
        val newReplays = listOf(
                ReplayInfo("6", "Game #6", 9822112760, 9822112760 + 871, "", Faction.PREDATOR, true),
                ReplayInfo("7", "Game #7", 8935181550, 8935181550 + 139, "", Faction.PREY, true),
                ReplayInfo("8", "Game #8", 5055362440, 5055362440 + 549, "", Faction.PREY, true))
        LocalReplayStore(context).saveList(newReplays)
        LocalReplayStore(context).getList{l: List<ReplayInfo> -> check = l.size == 9}
        Thread.sleep(100)
        assert(check)
    }
}