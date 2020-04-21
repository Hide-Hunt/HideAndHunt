package ch.epfl.sdp.authentication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.MainActivity
import ch.epfl.sdp.dagger.HideAndHuntApplication
import ch.epfl.sdp.databinding.ActivityLoginBinding
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    @Inject lateinit var connector:IUserConnector

    override fun onCreate(savedInstanceState: Bundle?) {
        // We have to handle the dependency injection before the call to super.onCreate
        (applicationContext as HideAndHuntApplication).appComponent.inject(this)
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
        testConnection()
    }

    private fun signIn(email:Editable, password:Editable, connector: IUserConnector) {
        connector.connect(email.toString(), password.toString())
        testConnection()
    }

    private fun testConnection() {
        Thread.sleep(100)
        if(User.connected) {
            finish()
        }
    }
}