package ch.epfl.sdp

object TestWait {
    /**
     * This function waits until `condition` returns true or `maxDuration` has elapsed by sleeping
     * steps of `timeStep`.
     *
     * This function assumes `maxDuration` is a multiple of `timeStep`
     *
     * @param maxDuration the maximum amount of time in millisecond to sleep
     * @param condition a function to stop waiting (if it returns true)
     * @param timeStep the duration of a sleep step in millisecond (default = 100)
     */
    fun wait(maxDuration: Long, condition: () -> Boolean, timeStep: Long = 100) {
        val nbSteps = maxDuration / timeStep
        for (x in 0 until nbSteps) {
            if (condition()) break
            Thread.sleep(timeStep)
        }
    }
}