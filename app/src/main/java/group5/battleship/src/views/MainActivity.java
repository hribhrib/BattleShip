package group5.battleship.src.views;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import group5.battleship.R;

public class MainActivity extends AppCompatActivity{
    EditText playername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        playername = (EditText) (findViewById(R.id.editText));
        playername.setBackgroundColor(Color.WHITE);
    }

    public void startWifiGame(View view) {
        Intent intent = new Intent(this, WifiManagerActivity.class);

        intent.putExtra("NAME", playername.getText().toString());
        intent.putExtra("WIFI", true);
        startActivity(intent);
    }

    public void startLocalGame(View view) {
        Intent intent = new Intent(this, SetShipsActivity.class);
        intent.putExtra("NAME", playername.getText().toString());
        intent.putExtra("WIFI", false);
        startActivity(intent);
    }









}
