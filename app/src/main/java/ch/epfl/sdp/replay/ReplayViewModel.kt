package ch.epfl.sdp.replay

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReplayViewModel : ViewModel() {
    val timeCursor = MutableLiveData<Int>()

    fun setTimeCursor(value: Int) {
        timeCursor.value = value
    }

    val trackedPlayer = MutableLiveData<Int>()

    fun setTrackedPlayer(playerID: Int) {
        trackedPlayer.value = playerID
    }
}
