package group5.battleship.src.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

import group5.battleship.R;

public class MainActivity extends AppCompatActivity{
    EditText playername;
    Intent intent;


    String playerNameHint;
    String soloGameText;
    String multiPlayerText;
    String statisticsText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("my Log", "OnCreate MainAct");

        super.onCreate(savedInstanceState);


        intent=null;
        setContentView(R.layout.activity_home_screen);
        playername = (EditText) (findViewById(R.id.textfieldName));
        playername.setBackgroundColor(Color.WHITE);
        playername.setTextColor(Color.BLACK);
        //playername.setHint(playerNameHint);

        // set the language
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        Configuration config = getBaseContext().getResources().getConfiguration();

        String lang = settings.getString("LANG", "");
        if (! "".equals(lang) && ! config.locale.getLanguage().equals(lang)) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }


        TextView gameTitle = (TextView) findViewById(R.id.game_Title);
        gameTitle.setText(R.string.app_name);

        Button vsFriend = (Button) findViewById(R.id.vsFriend);
        Button vsBotbtn = (Button) findViewById(R.id.vsBot);

        // set the text
        vsFriend.setText(R.string.vsFriend);
        vsBotbtn.setText(R.string.vsBot);



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

        //pass on settings
        intent.putExtra("sound",getIntent().getBooleanExtra("sound",true));
        intent.putExtra("language",getIntent().getStringExtra("language"));
        intent.putExtra("difficulty",getIntent().getStringExtra("difficulty"));
        startActivity(intent);

    }

    public void openPreferences (View view) {
        Intent intent = new Intent(this,PreferencesActivity.class);
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

    public void setLanguage() {

        String language = intent.getStringExtra("language");

        switch (language) {

            case "en" : ;
                break;

            case "de" : ;
                break;

        }


    }











}
