package ch.epfl.sdp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ch.epfl.sdp.databinding.ActivityMainBinding;
import ch.epfl.sdp.lobby.GameCreationActivity;
import ch.epfl.sdp.lobby.LobbyActivity;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        mainBinding.playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LobbyActivity.class);
                startActivity(intent);
            }
        });

        mainBinding.newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameCreationActivity.class);
                startActivity(intent);
            }
        });
    }
}
