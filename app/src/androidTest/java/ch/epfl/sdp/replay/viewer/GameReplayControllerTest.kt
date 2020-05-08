package ch.epfl.sdp.replay.viewer

import ch.epfl.sdp.replay.steps.ReplayStep
import org.junit.Assert.*
import org.junit.Test

class GameReplayControllerTest {
    private val firstDate = 1026211559 // 2002-07-09 10:45:59
    private val tenSeconds = 1026211569 // 2002-07-09 10:46:09

    @Test
    fun instantiatingGameReplayControllerShouldExecuteFirstStep() {
        var firstStepGotCalled = false
        val steps = listOf(
                object : ReplayStep {
                    override fun execute() { firstStepGotCalled = true }
                    override fun undo() { fail("First step got called: undo") }
                },
                object : ReplayStep {
                    override fun execute() { fail("Second step got called: execute") }
                    override fun undo() { fail("Second step got called: undo") }
                }
        )

        GameReplayController(firstDate, tenSeconds, steps)
        assert(firstStepGotCalled)
    }

    @Test
    fun goingToLastStepShouldExecuteAllSteps() {
        val calls = MutableList(4) {0}
        val steps = listOf(
                object : ReplayStep {
                    override fun execute() { calls[0]++ }
                    override fun undo() { fail("Step 1 got called: undo") }
                },
                object : ReplayStep {
                    override fun execute() { calls[1]++ }
                    override fun undo() { fail("Step 2 got called: undo") }
                },
                object : ReplayStep {
                    override fun execute() { calls[2]++ }
                    override fun undo() { fail("Step 3 got called: undo") }
                },
                object : ReplayStep {
                    override fun execute() { calls[3]++ }
                    override fun undo() { fail("Step 4 got called: undo") }
                }
        )

        val grc = GameReplayController(firstDate, tenSeconds, steps)
        grc.goToTime(firstDate+3)
        calls.forEach { assertEquals(1, it) }
    }

    @Test
    fun goingToPenultimateStepShouldExecuteAllStepsButTheLastOne() {
        val calls = MutableList(4) {0}
        val steps = listOf(
                object : ReplayStep {
                    override fun execute() { calls[0]++ }
                    override fun undo() { fail("Step 1 got called: undo") }
                },
                object : ReplayStep {
                    override fun execute() { calls[1]++ }
                    override fun undo() { fail("Step 2 got called: undo") }
                },
                object : ReplayStep {
                    override fun execute() { calls[2]++ }
                    override fun undo() { fail("Step 3 got called: undo") }
                },
                object : ReplayStep {
                    override fun execute() { fail("Step 4 got called: execute") }
                    override fun undo() { fail("Step 4 got called: undo") }
                }
        )

        val grc = GameReplayController(firstDate, tenSeconds, steps)
        grc.goToTime(firstDate+2)
        calls.take(3).forEach { assertEquals(1, it) }
        assertEquals(0, calls[3])
    }

    @Test
    fun goingToFirstStepFromTheLastOneShouldUndoAllStepsButTheFirstOne() {
        val executeCalls = MutableList(4) {0}
        val undoCalls = MutableList(4) {0}
        val steps = listOf(
                object : ReplayStep {
                    override fun execute() { executeCalls[0]++ }
                    override fun undo() { fail("Step 1 got called: undo") }
                },
                object : ReplayStep {
                    override fun execute() { executeCalls[1]++ }
                    override fun undo() { undoCalls[1]++ }
                },
                object : ReplayStep {
                    override fun execute() { executeCalls[2]++ }
                    override fun undo() { undoCalls[2]++ }
                },
                object : ReplayStep {
                    override fun execute() { executeCalls[3]++ }
                    override fun undo() { undoCalls[3]++ }
                }
        )

        val grc = GameReplayController(firstDate, tenSeconds, steps)

        grc.goToTime(firstDate+3)
        executeCalls.forEach { assertEquals(1, it) }
        undoCalls.forEach { assertEquals(0, it) }

        grc.goToTime(firstDate)
        undoCalls.take(1).forEach { assertEquals(0, it) }
        undoCalls.takeLast(3).forEach { assertEquals(1, it) }
    }

    @Test
    fun goingToSecondStepFromThePenultimateOneShouldUndoAllStepsButTheFirstTwo() {
        val executeCalls = MutableList(4) {0}
        val undoCalls = MutableList(4) {0}
        val steps = listOf(
                object : ReplayStep {
                    override fun execute() { executeCalls[0]++ }
                    override fun undo() { fail("Step 1 got called: undo") }
                },
                object : ReplayStep {
                    override fun execute() { executeCalls[1]++ }
                    override fun undo() { undoCalls[1]++ }
                },
                object : ReplayStep {
                    override fun execute() { executeCalls[2]++ }
                    override fun undo() { undoCalls[2]++ }
                },
                object : ReplayStep {
                    override fun execute() { executeCalls[3]++ }
                    override fun undo() { undoCalls[3]++ }
                }
        )

        val grc = GameReplayController(firstDate, tenSeconds, steps)

        grc.goToTime(firstDate+3)
        executeCalls.forEach { assertEquals(1, it) }
        undoCalls.forEach { assertEquals(0, it) }

        grc.goToTime(firstDate+1)
        undoCalls.take(2).forEach { assertEquals(0, it) }
        undoCalls.takeLast(2).forEach { assertEquals(1, it) }
    }
}