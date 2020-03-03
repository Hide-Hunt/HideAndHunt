package ch.epfl.sdp.predator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.epfl.sdp.R

/**
 * This activity only exists for developing purposes
 */
class PreyCatchFragmentTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.prey_catch_fragment_test_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, PreyCatchFragment.newInstance())
                    .commitNow()
        }
    }
}
