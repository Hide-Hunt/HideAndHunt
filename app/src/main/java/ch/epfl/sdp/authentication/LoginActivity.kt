package ch.epfl.sdp.authentication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.MainActivity
import ch.epfl.sdp.databinding.ActivityLoginBinding
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var connector:IUserConnector = FirebaseUserConnector()

    fun setOtherConnector(con: IUserConnector) {
        connector = con
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSubmitButton.setOnClickListener {
            signIn(userNameLogin.text, userPasswordLogin.text, connector)
        }
        binding.registerSubmitButton.setOnClickListener {
            register(userNameLogin.text, userPasswordLogin.text, userPseudoLogin.text, connector)
        }
    }

    private fun register(email:Editable, password:Editable, pseudo:Editable, connector:IUserConnector) {
        connector.register(email.toString(), password.toString(), pseudo.toString())
        Thread.sleep(2000)
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
    }

    private fun signIn(email:Editable, password:Editable, connector: IUserConnector) {
        connector.connect(email.toString(), password.toString())
        Thread.sleep(2000)
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intent)
    }

    private fun changeDummyText(text:String) {
        binding.loginTextResult.text = text
    }
}