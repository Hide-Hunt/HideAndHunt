package ch.epfl.sdp.authentication

import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
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
            register(userNameLogin.text, userPasswordLogin.text, connector)
        }
    }

    private fun register(email:Editable, password:Editable, connector:IUserConnector) {
        connector.register(email.toString(), password.toString(), "TestPseudo")
        if(User.connected)
            changeDummyText("Account created and logged in as " + User.pseudo)
        else
            changeDummyText("Account creation failed")
    }

    private fun signIn(email:Editable, password:Editable, connector: IUserConnector) {
        connector.connect(email.toString(), password.toString())
        if(User.connected)
            changeDummyText("User logged in as " + User.pseudo)
        else
            changeDummyText("Logging failed")
    }

    private fun changeDummyText(text:String) {
        binding.loginTextResult.text = text;
    }
}