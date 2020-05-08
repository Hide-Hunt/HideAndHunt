package ch.epfl.sdp.replay.steps

import org.junit.Test

import org.junit.Assert.*

class BatchStepTest {
    @Test
    fun executeWithoutStepDoNothingWithoutError() {
        BatchStep(listOf()).execute()
    }

    @Test
    fun undoWithoutStepDoNothingWithoutError() {
        BatchStep(listOf()).undo()
    }

    @Test
    fun executeWithOneStepShouldExecuteIt() {
        var called = false
        val step = object : ReplayStep {
            override fun execute() { called = true }
            override fun undo() { fail("Step 1 got called: undo") }
        }
        BatchStep(listOf(step)).execute()
        assert(called)
    }

    @Test
    fun undoWithOneStepShouldExecuteIt() {
        var called = false
        val step = object : ReplayStep {
            override fun execute() { fail("Step 1 got called: execute") }
            override fun undo() { called = true }
        }
        BatchStep(listOf(step)).undo()
        assert(called)
    }

    @Test
    fun executeShouldExecuteAllSteps() {
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
        BatchStep(steps).execute()
        calls.forEach { assertEquals(1, it) }
    }

    @Test
    fun undoShouldUndoAllSteps() {
        val calls = MutableList(4) {0}
        val steps = listOf(
                object : ReplayStep {
                    override fun execute() { fail("Step 1 got called: execute") }
                    override fun undo() { calls[0]++ }
                },
                object : ReplayStep {
                    override fun execute() { fail("Step 2 got called: execute") }
                    override fun undo() { calls[1]++ }
                },
                object : ReplayStep {
                    override fun execute() { fail("Step 3 got called: execute") }
                    override fun undo() { calls[2]++ }
                },
                object : ReplayStep {
                    override fun execute() { fail("Step 4 got called: execute") }
                    override fun undo() { calls[3]++ }
                }
        )
        BatchStep(steps).undo()
        calls.forEach { assertEquals(1, it) }
    }
}