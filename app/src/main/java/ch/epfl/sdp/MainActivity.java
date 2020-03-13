package ch.epfl.sdp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ch.epfl.sdp.databinding.ActivityMainBinding;
import ch.epfl.sdp.lobby.LobbyActivity;
import ch.epfl.sdp.lobby.global.GlobalLobbyActivity;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.playButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GlobalLobbyActivity.class);
            startActivity(intent);
        });
    }
}
