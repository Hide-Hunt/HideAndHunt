package ch.epfl.sdp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ch.epfl.sdp.databinding.ActivityLobbyBinding;

public class LobbyActivity extends AppCompatActivity {
    private ActivityLobbyBinding lobbyBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lobbyBinding = ActivityLobbyBinding.inflate(getLayoutInflater());
        setContentView(lobbyBinding.getRoot());

        lobbyBinding.startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LobbyActivity.this, PredatorActivity.class);
                startActivity(intent);
            }
        });
    }
}
