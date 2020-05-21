package ch.epfl.sdp.error

import java.io.Serializable

open class Error(val code: ErrorCode, val message: String) : Serializable