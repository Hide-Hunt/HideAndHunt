package ch.epfl.sdp.user

/**
 * Describe a User. As stored in the cache for example
 */
data class User (
        var name: String,
        var uid: Int
)