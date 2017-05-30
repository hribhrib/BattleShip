package group5.battleship.src.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import group5.battleship.R;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
    }

    public void closeInfo(View view) {
        Intent intent = new Intent(this, PreferencesActivity.class);
        startActivity(intent);

    }
}
