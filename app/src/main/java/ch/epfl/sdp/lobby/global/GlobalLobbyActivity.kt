package ch.epfl.sdp.lobby.global

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ch.epfl.sdp.R

class GlobalLobbyActivity : AppCompatActivity() {


    private lateinit var glf: GlobalLobbyFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val b = intent.extras
        setContentView(R.layout.activity_global_lobby)
        glf = GlobalLobbyFragment.newInstance()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, glf)
                    .commitNow()
        }
    }
}
