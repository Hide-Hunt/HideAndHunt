package ch.epfl.sdp.game

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.epfl.sdp.MainActivity
import ch.epfl.sdp.R
import ch.epfl.sdp.databinding.ActivityEndGameBinding
import ch.epfl.sdp.databinding.ActivityPreyBinding

class EndGameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEndGameBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEndGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBackToMain.setOnClickListener {
            val backIntent = Intent(this, MainActivity::class.java)
            startActivity(backIntent);
        }

        val durationInMs = intent.getLongExtra("duration", 0)
        val catchCount = intent.getIntExtra("catchcount", 0)

        binding.txtDurationGameover.text = getString(R.string.seconds_duration).format(durationInMs / 1000)
        binding.txtNbCatchesGameover.text = catchCount.toString()
    }
}
