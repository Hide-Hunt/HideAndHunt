package ch.epfl.sdp.authentication

import android.graphics.Bitmap

/**
 * Describe the interface for the user connexion
 */
interface IUserConnector {
    /**
     * Try to connect the user with the given information
     * @param email String: The user email
     * @param password String: Thus user password
     * @param successCallback Callback: Called when the user is successfully logged
     * @param errorCallback Callback: Called when an error occurs during connexion
     */
    fun connect(email: String, password: String, successCallback: () -> Unit, errorCallback: () -> Unit)

    /**
     * Disconnect the current user
     */
    fun disconnect()

    /**
     * Modify the current user
     * @param pseudo String: The new user's pseudo
     * @param profilePic Bitmap: The new user's profile picture
     * @param successCallback Callback: Called when the information are successfully updated
     * @param errorCallback Callback: Called if an error occurs
     */
    fun modify(pseudo: String?, profilePic: Bitmap?, successCallback: () -> Unit, errorCallback: () -> Unit)

    /**
     * Register a new user
     * @param email String: The new user's email
     * @param password String: The new user's password
     * @param successCallback Callback: Called when the user is successfully registered
     * @param errorCallback Callback: Called when an error occurs during registration
     */
    fun register(email: String, password: String, pseudo: String, successCallback: () -> Unit, errorCallback: () -> Unit)
}