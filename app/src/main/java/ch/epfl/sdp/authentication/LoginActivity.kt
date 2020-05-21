package ch.epfl.sdp.authentication

import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.R
import ch.epfl.sdp.dagger.HideAndHuntApplication
import ch.epfl.sdp.databinding.ActivityLoginBinding
import ch.epfl.sdp.user.UserCache
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

/**
 * Shows the login menu where the user can connect or register
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    @Inject lateinit var connector: IUserConnector
    private val cache: UserCache = UserCache()

    override fun onCreate(savedInstanceState: Bundle?) {
        // We have to handle the dependency injection before the call to super.onCreate
        (applicationContext as HideAndHuntApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSubmitButton.setOnClickListener {
            when {
                userNameLogin.text.isEmpty() -> userNameLogin.error = "Field is empty"
                userPasswordLogin.text.isEmpty() -> userPasswordLogin.error = "Field is empty"
                else -> signIn(userNameLogin.text, userPasswordLogin.text, connector)
            }
        }
        binding.registerSubmitButton.setOnClickListener {
            when {
                userPseudoLogin.text.isEmpty() -> userPseudoLogin.error = "Field is empty"
                userNameLogin.text.isEmpty() -> userNameLogin.error = "Field is empty"
                userPasswordLogin.text.isEmpty() -> userPasswordLogin.error = "Field is empty"
                else -> register(userNameLogin.text, userPasswordLogin.text, userPseudoLogin.text, connector)
            }
        }
        binding.loginTextResult.text = ""
    }

    private fun register(email: Editable, password: Editable, pseudo: Editable, connector: IUserConnector) {
        binding.loginTextResult.text = getString(R.string.registering_pending)
        connector.register(email.toString(), password.toString(), pseudo.toString(), { successfulLogin() }, { errorLogin() })
    }

    private fun signIn(email: Editable, password: Editable, connector: IUserConnector) {
        binding.loginTextResult.text = getString(R.string.connecting_pending)
        connector.connect(email.toString(), password.toString(), { successfulLogin() }, { errorLogin() })
    }

    private fun errorLogin() {
        binding.loginTextResult.text = ""
        AlertDialog.Builder(this).setMessage("Error: failed to connect")
                .setCancelable(false)
                .setPositiveButton("OK", null)
                .create()
                .show()
    }

    private fun successfulLogin() {
        cache.put(this)
        finish()
    }
}