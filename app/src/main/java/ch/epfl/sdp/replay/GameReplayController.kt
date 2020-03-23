package ch.epfl.sdp.replay

import ch.epfl.sdp.replay.steps.ReplayStep

class GameReplayController(
        private val firstTimestamp: Int,
        private val lastTimestamp: Int,
        private val history: List<ReplayStep>) {
    private var timeCursor: Int = firstTimestamp

    init {
        history.first().execute()
    }

    fun goToTime(timeTarget: Int) {
        minOf(maxOf(timeTarget, firstTimestamp), lastTimestamp).let {
            // Forward
            if (it > timeCursor) {
                while (it > timeCursor) {
                    timeCursor++
                    history[timeCursor - firstTimestamp].execute()
                }
            }
            // Backward
            else if (it < timeCursor) {
                while (it < timeCursor) {
                    history[timeCursor - firstTimestamp].undo()
                    timeCursor--
                }
            }
        }
    }
}