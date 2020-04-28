package ch.epfl.sdp.authentication

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.user.UserCache
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.OutputStream

class FirebaseUserConnector : IUserConnector {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private val storage = Firebase.storage

    override fun connect(email: String, password: String, successCallback: () -> Unit, errorCallback: () -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val authed = auth.currentUser
                User.email = email
                User.uid = authed!!.uid
                val maxSize: Long = 10 * 1024 * 1024
                storage.reference.child("profilePics").child(User.uid + ".png").getBytes(maxSize)
                .addOnSuccessListener {
                    User.profilePic = BitmapFactory.decodeByteArray(it, 0, it.size)
                }.addOnFailureListener {
                    User.profilePic = null
                }
                db.collection("user").document(User.uid).get().addOnSuccessListener { document ->
                    if (document != null) {
                        User.pseudo = document.data!!["pseudo"].toString()
                        User.connected = true
                        successCallback()
                    } else {
                        User.connected = false
                        errorCallback()
                    }
                }.addOnFailureListener { _ ->
                    User.connected = false
                    errorCallback()
                }
            } else {
                errorCallback()
            }
        }
    }

    override fun disconnect() {
        auth.signOut()
        User.connected = false
    }

    override fun modify(pseudo: String?, profilePic: Bitmap?, successCallback: () -> Unit, errorCallback: () -> Unit) {
        Log.d("CACHE", "MODIFYING")
        Log.d("CACHE", "ProfilePic null ? " + (profilePic == null))
        Log.d("CACHE", "MODIFYING")
        if(pseudo != null){
            Log.d("CACHE", "pseudo")
            val dataToAdd = hashMapOf("pseudo" to pseudo)
            db.collection("user").document(User.uid).set(dataToAdd).addOnCompleteListener() {
                if(it.isSuccessful)
                    successCallback()
                else
                    errorCallback()
            }
        }
        if(profilePic != null) {
            Log.d("CACHE", "pic")
            val metadata = storageMetadata {
                contentType = "image/png"
            }

            val profilePics = storage.reference.child("profilePics")
            val stream = ByteArrayOutputStream()
            profilePic.compress(Bitmap.CompressFormat.PNG, 90,  stream)
            val uploadTask = profilePics.child(User.uid + ".png").putBytes(stream.toByteArray(), metadata)
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
                User.email = email
                User.uid = authed!!.uid
                User.pseudo = pseudo
                val dataToAdd = hashMapOf("pseudo" to pseudo)
                db.collection("user").document(User.uid)
                        .set(dataToAdd)
                        .addOnSuccessListener {
                            User.connected = true
                            successCallback()
                        }
                        .addOnFailureListener {
                            User.connected = false
                            errorCallback()
                        }
            } else {
                User.connected = false
                errorCallback()
            }
        }
    }

}