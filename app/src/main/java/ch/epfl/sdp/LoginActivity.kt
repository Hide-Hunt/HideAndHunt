package ch.epfl.sdp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private var loginBinding: ActivityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(loginBinding.root)
        /*loginBinding.startGameButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@LobbyActivity, PredatorActivity::class.java)
            startActivity(intent)
        })*/
    }
}