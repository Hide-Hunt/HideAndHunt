package ch.epfl.sdp.replay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.epfl.sdp.databinding.ActivityManageReplaysBinding

class ManageReplaysActivity : AppCompatActivity() {
    private lateinit var binding: ActivityManageReplaysBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageReplaysBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
