package ch.epfl.sdp.error

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.MainActivity
import ch.epfl.sdp.databinding.ActivityErrorBinding

import kotlinx.android.synthetic.main.activity_error.*

class ErrorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityErrorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityErrorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val error = intent.getSerializableExtra("error") as Error?
        if (error != null) {
            binding.errorDetail.text = error.message
        }

        binding.homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    companion object {
        fun startWith(context: Context, error: Error) {
            val intent = Intent(context, ErrorActivity::class.java)
            intent.putExtra("error", error)
            context.startActivity(intent)
        }
    }
}
