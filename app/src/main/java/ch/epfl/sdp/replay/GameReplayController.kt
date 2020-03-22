package ch.epfl.sdp.replay

import ch.epfl.sdp.replay.steps.ReplayStep

class GameReplayController(private val initialTimestamp: Int, private val history: List<ReplayStep>) {
    private var timeCursor: Int = initialTimestamp

    fun goToTime(timeTarget: Int) {
        maxOf(timeTarget, initialTimestamp).let {
            // Forward
            while (it > timeCursor) {
                nextSecond()
            }

            // Backward
            while (it < timeCursor) {
                prevSecond()
            }

            history[timeCursor - initialTimestamp].execute()
        }
    }

    /**
     * Step one second forward by executing commands
     */
    fun nextSecond() {
        history[timeCursor - initialTimestamp].execute()
        timeCursor++
    }

    fun prevSecond() {
        history[timeCursor - initialTimestamp].undo()
        timeCursor--
    }
}