package ch.epfl.sdp.game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import ch.epfl.sdp.databinding.ActivityPredatorBinding;

public class PredatorActivity extends AppCompatActivity implements TargetSelectionFragment.OnTargetSelectedListener {
    private ActivityPredatorBinding binding;

    private int gameID;
    private Player player;
    private int targetID;
    private Location lastKnownLocation;
    private ArrayList<Player> players;
    private TargetSelectionFragment targetSelectionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPredatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get game information
        if (savedInstanceState == null) {
            // First load
            loadIntentPayload(getIntent());
            loadFragments();
        } else {
            // Restore state from previous activity
            loadBundle(savedInstanceState);
        }
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
        // create a FragmentManager
        FragmentManager fm = getSupportFragmentManager();
        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        // replace the FrameLayout with new Fragment
        targetSelectionFragment = TargetSelectionFragment.Companion.newInstance(players);
        fragmentTransaction.replace(binding.targetSelectionPlaceHolder.getId(), targetSelectionFragment);
        fragmentTransaction.commit(); // save the changes
    }

    private void fakeLoad() {
        gameID = 0;
        targetID = TargetSelectionFragment.NO_TARGET;
        player = new Player(0, Faction.PREDATOR);
        players = new ArrayList<>();
        for (int i=1; i<10; i++) {
            Faction faction = (Math.random() % 2 == 0) ? Faction.PREDATOR : Faction.PREY;
            players.add(new Player(i, faction));
        }
    }

    @Override
    public void onTargetSelected(int targetID) {
        this.targetID = targetID;
    }
}
