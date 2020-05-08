package ch.epfl.sdp.authentication

import android.graphics.Bitmap

interface IUserConnector {
    fun connect(email: String, password: String, successCallback: () -> Unit, errorCallback: () -> Unit)
    fun disconnect()
    fun modify(pseudo: String?, profilePic: Bitmap?, successCallback: () -> Unit, errorCallback: () -> Unit)
    fun register(email: String, password: String, pseudo: String, successCallback: () -> Unit, errorCallback: () -> Unit)
}