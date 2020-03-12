package ch.epfl.sdp.lobby.global

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.epfl.sdp.R
import ch.epfl.sdp.db.MockDB

class GlobalLobbyActivity : AppCompatActivity() {


    lateinit var glf: GlobalLobbyFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_global_lobby)
        glf = GlobalLobbyFragment()
        glf.repo = MockGlobalLobbyRepository(MockDB())
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, glf)
                    .commitNow()
        }
    }


}
