package ch.epfl.sdp.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import ch.epfl.sdp.databinding.ActivityPredatorBinding;

public class PredatorActivity extends AppCompatActivity {
    private ActivityPredatorBinding binding;

    private int gameID;
    private Player player;
    private int targetID;
    private Location lastKnownLocation;
    private ArrayList<Player> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPredatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get game information
        if (savedInstanceState == null) {
            // First load
            loadIntentPayload(getIntent());
        } else {
            // Restore state from previous activity
            loadBundle(savedInstanceState);
        }

        loadFragments();
    }

    private void loadIntentPayload(Intent intent) {
        // TODO get those information from intent
        fakeLoad();
    }

    private void loadBundle(Bundle bundle) {
        // TODO get those information from bundle
        fakeLoad();
    }

    private void loadFragments() {
    }

    private void fakeLoad() {
        gameID = 0;
        targetID = -1;
        player = new Player(0, Faction.PREDATOR);
        players = new ArrayList<>();
        for (int i=1; i<10; i++) {
            Faction faction = (Math.random() % 2 == 0) ? Faction.PREDATOR : Faction.PREY;
            players.add(new Player(i, faction));
        }
    }
}
