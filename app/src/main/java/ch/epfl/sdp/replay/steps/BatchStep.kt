package ch.epfl.sdp.replay.steps

class BatchStep(private val subSteps: List<ReplayStep>) : ReplayStep {
    override fun execute() = subSteps.forEach { it.execute() }
    override fun undo() = subSteps.forEach { it.undo() }
}