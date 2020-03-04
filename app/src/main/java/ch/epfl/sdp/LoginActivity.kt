package ch.epfl.sdp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*loginBinding.startGameButton.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@LobbyActivity, PredatorActivity::class.java)
            startActivity(intent)
        })*/
    }
}