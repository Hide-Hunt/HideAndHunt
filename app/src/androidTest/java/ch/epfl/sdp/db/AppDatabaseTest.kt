package ch.epfl.sdp.db

import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.InternalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test

class AppDatabaseTest {
    @InternalCoroutinesApi
    @Test
    fun gettingDBInstanceMultipleTimesShouldReturnTheSameOne() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        val i1 = AppDatabase.instance(ctx)
        val i2 = AppDatabase.instance(ctx)
        assertEquals(i1, i2)
    }
}