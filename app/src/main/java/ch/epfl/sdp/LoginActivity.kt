package ch.epfl.sdp

import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() =_binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSubmitButton.setOnClickListener {
            signIn(userNameLogin.text, userPasswordLogin.text)
        }
        binding.registerSubmitButton.setOnClickListener {
            register(userNameLogin.text, userPasswordLogin.text)
        }
    }

    private fun register(email:Editable, password:Editable) {
        auth.createUserWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        changeDummyText("User " + user?.email + " created and logged-in")
                    } else {
                        changeDummyText("Creation of user failed")
                    }
                }
    }

    private fun signIn(email:Editable, password:Editable) {
        auth.signInWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Log.d("DEBUG", "User login success")
                        changeDummyText("User " + user?.email + " logged in")
                    } else {
                        Log.d("DEBUG", "User login failure")
                        changeDummyText("Logged in failed")
                    }
                }
    }

    private fun changeDummyText(text:String) {
        binding.loginTextResult.text = text;
    }
}