package ch.epfl.sdp.replay

interface ReplayStep {
    fun execute()
    fun undo()

    object NOP : ReplayStep {
        override fun execute() {}
        override fun undo() {}
    }
}