package group5.battleship.src.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import group5.battleship.R;

public class HighScoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        TextView countTotalGames = (TextView)findViewById(R.id.countTotalGames);
        TextView countTotalGamesWon = (TextView)findViewById(R.id.CountTotalGamesWon);
        TextView countTotalGamesLost = (TextView)findViewById(R.id.countTotalGamesLost);
        TextView countShortestGame = (TextView)findViewById(R.id.shortestGame);

        countTotalGames.setText(getStats(this,"totalGamesPlayed"));
        countTotalGamesWon.setText(getStats(this,"totalGamesWon"));
        countTotalGamesLost.setText(getStats(this,"totalGamesLost"));
        countShortestGame.setText((getStats(this,"shortestGame")));

    }
    public String getStats (Context context, String key){
        SharedPreferences prefs;
        String value;
        prefs = context.getSharedPreferences("stats",Context.MODE_PRIVATE);
        value = Integer.toString(prefs.getInt(key,0));
        return value;
    }
 }
