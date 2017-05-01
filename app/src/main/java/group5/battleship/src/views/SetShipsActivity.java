package group5.battleship.src.views;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import group5.battleship.R;


public class SetShipsActivity extends AppCompatActivity {
    String ships = ""; //XYXYXY
    int MAX_SHIPS = 3;
    int currentShips = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_ships);

        Context context = getApplicationContext();
        CharSequence text = "Click on the area to set 3 ships!";
        int duration = Toast.LENGTH_SHORT;

        Toast.makeText(context, text, duration).show();


    }

    public void cellClick(View view) {
        TextView tv = (TextView) findViewById(view.getId());


        if (currentShips < MAX_SHIPS) {
            if (ships.length() < 2) {
                ships = (ships + (String) view.getTag());
                currentShips++;

                tv.setTextColor(Color.WHITE);
                tv.setText("o");

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

                    tv.setText("o");


                    if (currentShips == MAX_SHIPS) {

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

                    }

                }
            }

        } else if (currentShips == MAX_SHIPS) {

            // this code is not needed at the moment

            /*Context context = getApplicationContext();
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

        Log.d("My Log", String.valueOf(getIntent().getBooleanExtra("WIFI", true)));

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("SHIPS", ships);
        intent.putExtra("NAME", getIntent().getStringExtra("NAME"));
        intent.putExtra("HostAddress", getIntent().getStringExtra("HostAddress")); //Address of the host
        intent.putExtra("IsHost", getIntent().getBooleanExtra("IsHost", true));   //Is this device the host
        intent.putExtra("Connected", true); //Was connection succesul
        intent.putExtra("WIFI", getIntent().getBooleanExtra("WIFI", true));



        startActivity(intent);

    }

    public void showStartButton () {

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


}
