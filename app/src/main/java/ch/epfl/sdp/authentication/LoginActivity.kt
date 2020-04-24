package ch.epfl.sdp.authentication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
        binding.loginTextResult.text = ""
    }

    private fun register(email:Editable, password:Editable, pseudo:Editable, connector:IUserConnector) {
        binding.loginTextResult.text = "Registering..."
        connector.register(email.toString(), password.toString(), pseudo.toString(), {successfulLogin()}, {errorLogin()})
    }

    private fun signIn(email:Editable, password:Editable, connector: IUserConnector) {
        binding.loginTextResult.text = "Connecting..."
        connector.connect(email.toString(), password.toString(), {successfulLogin()}, {errorLogin()})
    }

    private fun errorLogin() {
        binding.loginTextResult.text = ""
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Error: failed to connect")
                .setCancelable(false)
                .setPositiveButton("OK",null)
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    private fun successfulLogin() {
        finish()
    }
}