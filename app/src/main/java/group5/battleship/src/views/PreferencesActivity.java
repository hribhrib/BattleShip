package group5.battleship.src.views;

import android.content.Intent;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

import java.util.Locale;

import group5.battleship.R;

public class PreferencesActivity extends AppCompatActivity {

    private String language;
    private boolean sound;
    private String difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        // set defaults
        language = "en";
        sound = true;
        difficulty = "Easy";
    }

    public void closePreferences(View view) {

        Intent intent = new Intent(this, MainActivity.class);

        // give the settings as extra
        intent.putExtra("sound", sound);
        intent.putExtra("language", language);
        intent.putExtra("difficulty", difficulty);


        startActivity(intent);

    }

    public void openInfo(View view) {

        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }


    //to make sure you get back to the Main Activity with this button
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void changeLanguage(View view) {

        Spinner langSelect = (Spinner) findViewById(R.id.langSelection);

        int position = langSelect.getSelectedItemPosition();

        switch (position) {

            case 0: // english

                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                        .putString("LANG", "en").commit();
                setLangRecreate("en");
                return;
            case 1: // german
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                        .putString("LANG", "de").commit();
                setLangRecreate("de");
                break;

            default: // set english to default
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                        .putString("LANG", "en").commit();
                setLangRecreate("en");
                break;
        }


    }

    public void setLangRecreate(String langval) {

        Configuration config = getBaseContext().getResources().getConfiguration();

        Locale locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;

        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        recreate();

    }


}

