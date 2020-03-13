package ch.epfl.sdp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import ch.epfl.sdp.databinding.ActivityMainBinding;
import ch.epfl.sdp.lobby.GameCreationActivity;
import ch.epfl.sdp.lobby.LobbyActivity;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.playButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LobbyActivity.class);
            startActivity(intent);
        });

        binding.newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameCreationActivity.class);
                startActivity(intent);
            }
        });
    }
}
