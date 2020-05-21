package ch.epfl.sdp.dagger.modules

import ch.epfl.sdp.db.AppDatabase
import org.junit.Assert.assertTrue
import org.junit.Test

class DBModuleTest {

    @Test
    fun testProvidesAppDatabaseCompanion() {
        val repo = DBModule()
        assertTrue(repo.providesAppDatabaseCompanion() is AppDatabase.Companion)
    }

}