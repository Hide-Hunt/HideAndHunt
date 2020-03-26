package ch.epfl.sdp.authentication

object User {
    var pseudo: String = ""
    var email: String = ""
    var uid: String = ""
    @Volatile var connected = false
}