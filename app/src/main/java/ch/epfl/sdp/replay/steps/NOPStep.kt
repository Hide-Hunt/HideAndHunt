package ch.epfl.sdp.replay.steps

class NOPStep : ReplayStep {
    override fun execute() = Unit
    override fun undo() = Unit
}