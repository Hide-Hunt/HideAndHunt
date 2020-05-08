package ch.epfl.sdp.replay.steps

interface ReplayStep {
    fun execute()
    fun undo()
}