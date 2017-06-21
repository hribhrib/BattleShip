package group5.battleship.src.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import group5.battleship.R;

public class HighScoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        TextView countTotalGames = (TextView)findViewById(R.id.countTotalGames);
        TextView countTotalGamesWon = (TextView)findViewById(R.id.CountTotalGamesWon);


        countTotalGames.setText(getStats(this,"totalGamesPlayed"));
        countTotalGamesWon.setText(getStats(this,"totalGamesWon"));


        // set the language
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this); // get the stored language setting
        Configuration config = getBaseContext().getResources().getConfiguration(); // load the old config

        String lang = settings.getString("LANG", "");
        if (!"".equals(lang) && !config.locale.getLanguage().equals(lang)) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }

        TextView totalGames = (TextView) findViewById(R.id.games_played);
        TextView gamesWon = (TextView) findViewById(R.id.games_won);
        /*TextView gamesLost = (TextView) findViewById(R.id.games_lost);
        TextView shortestGame = (TextView) findViewById(R.id.CountShortestGame);*/
        Button back = (Button) findViewById(R.id.button_back);


        totalGames.setText(R.string.total_games);
        gamesWon.setText(R.string.games_won);
       /* gamesLost.setText(R.string.games_lost);
        shortestGame.setText(R.string.shortest_game);*/
        back.setText(R.string.button_back);


    }

    public String getStats (Context context, String key){

        SharedPreferences prefs;
        String value;
        prefs = context.getSharedPreferences("stats",Context.MODE_PRIVATE);
        value = Integer.toString(prefs.getInt(key,0));
        return value;

    }

    public void back (View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


 }
