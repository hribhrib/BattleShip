package group5.battleship.src.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.Locale;

import group5.battleship.R;

public class PreferencesActivity extends AppCompatActivity {

    private String language;
    private boolean sound;
    private String difficulty;
    private int langPosition;
    private boolean positionChanged = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        // set defaults
        CheckBox soundb = (CheckBox) findViewById(R.id.soundCheckbox);
        difficulty = "Easy";


        // set the spinner position
        Spinner langSelect = (Spinner) findViewById(R.id.langSelection);
        langSelect.setSelection(langPosition);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        int spinnerValue = sharedPref.getInt("userChoiceSpinner", -1);
        if (spinnerValue != -1) {
            // set the value of the spinner
            langSelect.setSelection(spinnerValue);
        }
        Log.d("POS", " " + spinnerValue);

        // set the sound preferences
        boolean sound = sharedPref.getBoolean("sound", true);
        if (sound) {
            soundb.setChecked(true);
        } else {
            soundb.setChecked(false);
        }


        // set the difficulties
        RadioButton diffEasy = (RadioButton) findViewById(R.id.diffEasy);
        RadioButton diffMid = (RadioButton) findViewById(R.id.diffMedium);
        RadioButton diffHard = (RadioButton) findViewById(R.id.diffHard);

        int diff = sharedPref.getInt("diff", 10);

        switch (diff){

            case 3: diffHard.setChecked(true);
                break;

            case 5: diffMid.setChecked(true);
                break;

            case 10: diffEasy.setChecked(true);
                break;
        }



    }

    public void closePreferences(View view) {

        Intent intent = new Intent(this, MainActivity.class);

        // give the settings as extra
        intent.putExtra("sound", sound);
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
                break;
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

        // safe the spinner position
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEditor = sharedPref.edit();
        prefEditor.putInt("userChoiceSpinner", position);
        Log.d("Position nr one", "" + position);
        prefEditor.commit();


    }

    public void changeSound(View view) {

        CheckBox sound = (CheckBox) findViewById(R.id.soundCheckbox);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (sound.isChecked()) {
            // sound on

            sharedPreferences.edit().putBoolean("sound", true).commit();

        } else {
            //sound off
            sharedPreferences.edit().putBoolean("sound", false).commit();
        }

    }

    public void changeDifficulty(View view) {

        RadioButton diffEasy = (RadioButton) findViewById(R.id.diffEasy);
        RadioButton diffMid = (RadioButton) findViewById(R.id.diffMedium);
        RadioButton diffHard = (RadioButton) findViewById(R.id.diffHard);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (diffEasy.isChecked()) {

            sharedPreferences.edit().putInt("diff", 10).commit();

        } else if (diffMid.isChecked()) {

            sharedPreferences.edit().putInt("diff", 5).commit();

        } else if (diffHard.isChecked()) {

            sharedPreferences.edit().putInt("diff", 3).commit();
        }

    }

    // safe the last setting to default when starting up again
    public void setLangRecreate(String langval) {

        Configuration config = getBaseContext().getResources().getConfiguration();

        Locale locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;

        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        recreate();


    }


}

