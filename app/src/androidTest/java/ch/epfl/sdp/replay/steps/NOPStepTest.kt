package ch.epfl.sdp.replay.steps

import org.junit.Test

class NOPStepTest {

    @Test
    fun executeShouldDoNothingWithoutError() {
        NOPStep().execute()
    }

    @Test
    fun undoShouldDoNothingWithoutError() {
        NOPStep().undo()
    }
}