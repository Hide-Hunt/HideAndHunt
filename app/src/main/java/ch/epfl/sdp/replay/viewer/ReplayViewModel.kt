package ch.epfl.sdp.replay.viewer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ReplayViewModel : ViewModel() {
    val timeCursor = MutableLiveData<Int>()
    val trackedPlayer = MutableLiveData<Int>()
}
