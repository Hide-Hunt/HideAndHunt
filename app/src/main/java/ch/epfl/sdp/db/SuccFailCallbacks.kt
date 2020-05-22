package ch.epfl.sdp.db

class SuccFailCallbacks {
    data class UnitSuccFailCallback(val success: UnitCallback = {}, val failure: UnitCallback = {})
    data class SuccFailCallback<T>(val success: Callback<T>, val failure: UnitCallback = {})
}