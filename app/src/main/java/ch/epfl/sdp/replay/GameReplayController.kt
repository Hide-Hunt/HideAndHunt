package ch.epfl.sdp.replay

class GameReplayController(private val history: List<ReplayStep>) {
    private var timeCursor: Int = 0

    fun goToTime(timeTarget: Int) {
        // Forward
        while (timeTarget > timeCursor) {
            nextSecond()
        }

        // Backward
        while (timeTarget < timeCursor) {
            prevSecond()
        }
    }

    /**
     * Step one second forward by executing commands
     */
    fun nextSecond() {
        history[timeCursor].execute()
        timeCursor++
    }

    fun prevSecond() {
        history[timeCursor].undo()
        timeCursor--
    }
}