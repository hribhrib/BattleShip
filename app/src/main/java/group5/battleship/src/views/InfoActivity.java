package group5.battleship.src.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import group5.battleship.R;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

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

        // get the items of the GUI
        TextView developer2 = (TextView) findViewById(R.id.entwickler2);
        TextView developer = (TextView) findViewById(R.id.entwickler);
        TextView devGui = (TextView) findViewById(R.id.entwicklerGui);
        TextView gameFrom = (TextView) findViewById(R.id.gameFrom);
        TextView themeSong = (TextView) findViewById(R.id.themeSong);
        TextView architect = (TextView) findViewById(R.id.architect);
        TextView organisation = (TextView) findViewById(R.id.organisation);

        developer2.setText(R.string.developer);
        developer.setText(R.string.developer);
        devGui.setText(R.string.developerGui);
        gameFrom.setText(R.string.gameFrom);
        themeSong.setText(R.string.themeSong);
        architect.setText(R.string.architect);
        organisation.setText(R.string.organisation);



    }

    public void closeInfo(View view) {
        Intent intent = new Intent(this, PreferencesActivity.class);
        startActivity(intent);

    }
}
