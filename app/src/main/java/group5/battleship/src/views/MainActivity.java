package group5.battleship.src.views;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import group5.battleship.R;

public class MainActivity extends AppCompatActivity{
    EditText playername;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("my Log", "OnCreate MainAct");


        super.onCreate(savedInstanceState);
        intent=null;
        setContentView(R.layout.activity_home_screen);
        playername = (EditText) (findViewById(R.id.editText));
        playername.setBackgroundColor(Color.WHITE);
        playername.setTextColor(Color.BLACK);

    }

    public void startWifiGame(View view) {
        Log.d("my Log", "start Wifi MainAct");
        intent = new Intent(this, WifiManagerActivity.class);
        intent.putExtra("NAME", playername.getText().toString());
        intent.putExtra("WIFI", true);
        startActivity(intent);
    }

    public void startLocalGame(View view) {
        intent = new Intent(this, SetShipsActivity.class);
        intent.putExtra("NAME", playername.getText().toString());
        intent.putExtra("WIFI", false);
        startActivity(intent);

    }


    public void showInfo(View view) {
        intent = new Intent(this, InfoActivity.class);
        startActivity(intent);

    }

    public void viewStats(View view){
        Intent intent = new Intent(this,HighScoresActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        //Disable back button
    }











}
