package ch.epfl.sdp.authentication

interface IUserConnector {
    fun connect(email: String, password: String): Boolean
    fun disconnect(): Boolean
    fun register(email: String, password: String, pseudo: String): Boolean
}