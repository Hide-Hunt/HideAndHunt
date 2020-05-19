package ch.epfl.sdp.error

import org.junit.Assert.*
import org.junit.Test

class ErrorTest {
    @Test
    fun codeGetterReturnsSetValue() {
        assertEquals(ErrorCode.INVALID_ACTIVITY_PARAMETER, Error(ErrorCode.INVALID_ACTIVITY_PARAMETER, "").code)
    }

    @Test
    fun messageGetterReturnsSetValue() {
        assertEquals("s0m3 3rr0r m5g", Error(ErrorCode.INVALID_ACTIVITY_PARAMETER, "s0m3 3rr0r m5g").message)
    }
}