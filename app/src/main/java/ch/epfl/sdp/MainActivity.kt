package ch.epfl.sdp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
   // private var mainBinding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var mainBinding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        mainBinding.playButton.setOnClickListener {
            val intent = Intent(this@MainActivity, LobbyActivity::class.java)
            //val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}