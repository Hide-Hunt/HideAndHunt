package ch.epfl.sdp.authentication

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import java.io.ByteArrayOutputStream

class FirebaseUserConnector : IUserConnector {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private val storage = Firebase.storage

    override fun connect(email: String, password: String, successCallback: () -> Unit, errorCallback: () -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val authed = auth.currentUser
                LocalUser.email = email
                LocalUser.uid = authed!!.uid
                val maxSize: Long = 10 * 1024 * 1024
                storage.reference.child("profilePics").child(LocalUser.uid + ".png").getBytes(maxSize)
                .addOnSuccessListener {
                    LocalUser.profilePic = BitmapFactory.decodeByteArray(it, 0, it.size)
                }.addOnFailureListener {
                    LocalUser.profilePic = null
                }
                db.collection("user").document(LocalUser.uid).get().addOnSuccessListener { document ->
                    if (document != null) {
                        LocalUser.pseudo = document.data!!["pseudo"].toString()
                        LocalUser.connected = true
                        successCallback()
                    } else {
                        LocalUser.connected = false
                        errorCallback()
                    }
                }.addOnFailureListener { _ ->
                    LocalUser.connected = false
                    errorCallback()
                }
            } else {
                errorCallback()
            }
        }
    }

    override fun disconnect() {
        auth.signOut()
        LocalUser.connected = false
    }

    override fun modify(pseudo: String?, profilePic: Bitmap?, successCallback: () -> Unit, errorCallback: () -> Unit) {
        if(pseudo != null){
            val dataToAdd = hashMapOf("pseudo" to pseudo)
            db.collection("user").document(LocalUser.uid).set(dataToAdd).addOnCompleteListener() {
                if(it.isSuccessful)
                    successCallback()
                else
                    errorCallback()
            }
        }
        if(profilePic != null) {
            val metadata = storageMetadata {
                contentType = "image/png"
            }

            val profilePics = storage.reference.child("profilePics")
            val stream = ByteArrayOutputStream()
            profilePic.compress(Bitmap.CompressFormat.PNG, 90,  stream)
            val uploadTask = profilePics.child(LocalUser.uid + ".png").putBytes(stream.toByteArray(), metadata)
            uploadTask.addOnSuccessListener {
                successCallback()
            }.addOnFailureListener {
                errorCallback()
            }
        }
        else if(pseudo == null)
            successCallback()
    }

    override fun register(email: String, password: String, pseudo: String, successCallback: () -> Unit, errorCallback: () -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful){
                val authed = auth.currentUser
                LocalUser.email = email
                LocalUser.uid = authed!!.uid
                LocalUser.pseudo = pseudo
                val dataToAdd = hashMapOf("pseudo" to pseudo)
                db.collection("user").document(LocalUser.uid)
                        .set(dataToAdd)
                        .addOnSuccessListener {
                            LocalUser.connected = true
                            successCallback()
                        }
                        .addOnFailureListener {
                            LocalUser.connected = false
                            errorCallback()
                        }
            } else {
                LocalUser.connected = false
                errorCallback()
            }
        }
    }
}