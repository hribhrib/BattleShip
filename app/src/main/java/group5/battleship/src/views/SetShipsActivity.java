package group5.battleship.src.views;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.security.cert.Extension;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

import group5.battleship.R;


public class SetShipsActivity extends AppCompatActivity {
    String ships = ""; //XYXYXY
    int MAX_SHIPS = 3;
    int currentShips = 0;
    ArrayList<TextView> textViews = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_ships);

        Context context = getApplicationContext();
        CharSequence text = "Click on the area to set 3 ships!";
        int duration = Toast.LENGTH_SHORT;

        //Toast.makeText(context, text, duration).show();

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

        // get all elements of the GUI
        TextView setShips = (TextView) findViewById(R.id.setShips);
        TextView acceptText = (TextView) findViewById(R.id.acceptText);
        Button yesBtn = (Button) findViewById(R.id.yesbtn);
        Button noBtn = (Button) findViewById(R.id.nobtn);


        //Get the text fot the GUI Items
        setShips.setText(R.string.setShips);
        acceptText.setText(R.string.acceptText);
        yesBtn.setText(R.string.yesBtn);
        noBtn.setText(R.string.noBtn);

    }


    @Override
    protected void onResume(){
        super.onResume();
        Log.d("my tag", getIntent().getStringExtra("NAME")+ " debug");
        Log.d("my tag", "Resume in set ship");
        ships="";
        currentShips=0;

    }



    public void cellClick(View view) {
        TextView tv = (TextView) findViewById(view.getId());


        if (currentShips < MAX_SHIPS) {
            if (ships.length() < 2) {
                ships = (ships + (String) view.getTag());
                currentShips++;

                tv.setTextColor(Color.WHITE);
                //tv.setText("o");
                tv.setBackgroundResource(R.mipmap.sea_ship);
                storeTextview(tv);

            } else {
                boolean shipSet = false;
                boolean dublicate = false;
                for (int i = 0; i < ships.length() - 1; i = i + 2) {
                    String tmp = (ships.substring(i, i + 2));
                    if (!tmp.equals((String) view.getTag())) {
                        shipSet = true;
                    } else {
                        dublicate = true;
                    }
                }
                if (shipSet == true && dublicate == false) {
                    ships = (ships + (String) view.getTag());
                    currentShips++;

                    tv.setTextColor(Color.WHITE);
                    //tv.setText("o");
                    tv.setBackgroundResource(R.mipmap.sea_ship);
                    storeTextview(tv);

                    if (currentShips == MAX_SHIPS) {


                        // set the text and the button visible to confirm the arrangement

                        TextView setShipsText = (TextView) findViewById(R.id.setShips);
                        setShipsText.setVisibility(View.INVISIBLE);

                        TextView acceptText = (TextView) findViewById(R.id.acceptText);
                        acceptText.setVisibility(View.VISIBLE);

                        Button yesbtn = (Button) findViewById(R.id.yesbtn);
                        Button nobtn = (Button) findViewById(R.id.nobtn);

                        yesbtn.setVisibility(View.VISIBLE);
                        nobtn.setVisibility(View.VISIBLE);


/*

                        // Dialog to confirm the arrangement of the ships
                        new AlertDialog.Builder(SetShipsActivity.this)
                                .setTitle("All ships are set")
                                .setMessage("Are you sure you want to keep this arrangement?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with showing the Startgame Button
                                        showStartButton();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // reset the ships
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
*/

                    }
                }
            }

        } else if (currentShips == MAX_SHIPS) {

           /*

            // this code is not needed at the moment

            Context context = getApplicationContext();
            CharSequence text = "All ships are set";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            Button b15 = (Button) findViewById(R.id.startButton);
            b15.setVisibility(View.VISIBLE);

            View table = findViewById(R.id.table);

            table.setVisibility(View.INVISIBLE);
            */


            


        }
    }

    public void settingFinished(View view) {

        Log.d("My Log f", String.valueOf(getIntent().getBooleanExtra("WIFI", false)));
        Log.d("My Log t", String.valueOf(getIntent().getBooleanExtra("WIFI", true)));
        Log.d("My Log isHost f", String.valueOf(getIntent().getBooleanExtra("IsHost", false)));
        Log.d("My Log isHost t", String.valueOf(getIntent().getBooleanExtra("IsHost", true)));

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("SHIPS", ships);
        intent.putExtra("NAME", getIntent().getStringExtra("NAME"));
        intent.putExtra("HostAddress", getIntent().getStringExtra("HostAddress")); //Address of the host
        intent.putExtra("IsHost", getIntent().getBooleanExtra("IsHost", false));   //Is this device the host
        intent.putExtra("Connected", true); //Was connection succesul
        intent.putExtra("WIFI", getIntent().getBooleanExtra("WIFI", false));

        startActivity(intent);

    }

    public void showStartButton() {
        Context context = getApplicationContext();
        CharSequence text = "All ships are set";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        Button b15 = (Button) findViewById(R.id.startButton);
        b15.setVisibility(View.VISIBLE);

        View table = findViewById(R.id.table);

        table.setVisibility(View.INVISIBLE);
    }

    public void resetShips(View view) {

        TextView tmp;

        for (int i = textViews.size() - 1; i >= 0; i--) {
            Log.d("Size Array ", String.valueOf(i));
            tmp = textViews.get(i);
            tmp.setBackgroundResource(R.mipmap.meer_neu);
            //tmp.setText("");
            //textViews.remove(i);

        }

        // clear all variables
        textViews.clear();
        ships = "";
        currentShips = 0;

        //recreate the initial view
        TextView setShipsText = (TextView) findViewById(R.id.setShips);
        setShipsText.setVisibility(View.VISIBLE);

        TextView acceptText = (TextView) findViewById(R.id.acceptText);
        acceptText.setVisibility(View.INVISIBLE);

        Button yesbtn = (Button) findViewById(R.id.yesbtn);
        Button nobtn = (Button) findViewById(R.id.nobtn);

        yesbtn.setVisibility(View.INVISIBLE);
        nobtn.setVisibility(View.INVISIBLE);

    }

    public void storeTextview(TextView tv) {
        // store the textview
        textViews.add(tv);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(this, MainActivity.class);
        startActivity(back);

    }



}
