package group5.battleship.src.views;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import group5.battleship.R;
import group5.battleship.src.logic.Battlefield;
import group5.battleship.src.logic.Cordinate;
import group5.battleship.src.logic.Ship;

// this activity is meant for different shiptypes, the standart gamemode should use the old version of this activity
public class SetShipsActivity extends AppCompatActivity {
    String ships = ""; //XYXYXY
    int MAX_SHIPS = 6;
    int currentShips = 0;
    int activeShip = 0; // 0 = undefined, 1 = small, 3 = medium, 5 = big
    String tempDirection;
    ArrayList<TextView> textViews = new ArrayList<>();
    Battlefield tmpBattlefield = new Battlefield(8);
    int availableSmallShips = 3;
    int availableMedShips = 2;
    int availableBigShips = 1;
    // TODO: 03.06.2017 should get the max_ships and size of battlefield as extra



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: 03.06.2017 contentview should be received from the button in main 
        setContentView(R.layout.activity_set_ships_2);

        TextView t_small = (TextView)findViewById(R.id.numberSmallShips);
        TextView t_med = (TextView)findViewById(R.id.numbeMedShips);
        TextView t_big = (TextView)findViewById(R.id.numberBigShips);
        t_small.setText(String.valueOf(availableSmallShips));
        t_med.setText(String.valueOf(availableMedShips));
        t_big.setText(String.valueOf(availableBigShips));


        Context context = getApplicationContext();
        CharSequence text = "Click on the area to set your ships!";
        int duration = Toast.LENGTH_SHORT;

        Toast.makeText(context, text, duration).show();

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("my tag", getIntent().getStringExtra("NAME") + " debug");
        Log.d("my tag", "Resume in set ship");
        ships = "";
        currentShips = 0;

    }

    // TODO: 03.06.2017 add a textview and a 'if-else' to the layout that dec the number of available ships of a special type after setting  
    public void chooseShip(View view) {
        tempDirection = "v";
        ImageButton small = (ImageButton) findViewById(R.id.smallShip);
        ImageButton medium = (ImageButton) findViewById(R.id.mediumShip);
        ImageButton big = (ImageButton) findViewById(R.id.bigShip);

        if (availableSmallShips>0){
        small.setVisibility(View.VISIBLE);}
        if (availableMedShips>0){
        medium.setVisibility(View.VISIBLE);}
        if (availableBigShips>0){
        big.setVisibility(View.VISIBLE);}

        switch (view.getId()) {
            case R.id.smallShip:
                activeShip = 1;
                break;
            case R.id.mediumShip:
                activeShip = 3;
                break;
            case R.id.bigShip:
                activeShip = 5;
        }
        small.setVisibility(View.INVISIBLE);
        medium.setVisibility(View.INVISIBLE);
        big.setVisibility(View.INVISIBLE);

    }

    public void rotate() {
        if (tempDirection == "v"){
            tempDirection = "h";
        } else {
            tempDirection = "v";
        }
    }
    // TODO: 03.06.2017 add a rotate button to activity_set_ships_2 as well as a rotated imageview of the selected ship-


    public void cellClick(View view) {

            if (currentShips < MAX_SHIPS) {

                Cordinate mainCordinate = new Cordinate((String) view.getTag());
                Ship ship = new Ship(activeShip, tempDirection, mainCordinate);
                if (tmpBattlefield.checkSpace(ship)) {
                    tmpBattlefield.addShip(ship);
                    currentShips++;

                    // // TODO: 03.06.2017 - add assets for different ships, and place it over the used cordinates -
                   // updateBattlefield(ship, view);
                    /*switch (activeShip) {
                        case 1:
                            availableSmallShips--;
                            updateTextView(1);
                            break;
                        case 3:
                            availableMedShips--;
                            updateTextView(3);
                            break;
                        case 5:
                            availableBigShips--;
                            updateTextView(5);
                            break;*/
                    }
                    /*ImageButton small = (ImageButton) findViewById(R.id.smallShip);
                    ImageButton medium = (ImageButton) findViewById(R.id.mediumShip);
                    ImageButton big = (ImageButton) findViewById(R.id.bigShip);


                    small.setVisibility(View.VISIBLE);

                    medium.setVisibility(View.VISIBLE);

                    big.setVisibility(View.VISIBLE);

*/

            } else {
                Context context = getApplicationContext();
                CharSequence text = "Ship cant be set here!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }


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
// TODO: 03.06.2017 putSeriazable with the battlefield and the ship objects 
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
        // TODO: 03.06.2017 re-write this method for different ship types 
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

    public void updateTempBattlefield(Cordinate c, String tempDirection) {

    }
    public void updateTextView (int activeShip) {
        TextView t_small = (TextView)findViewById(R.id.numberSmallShips);
        TextView t_med = (TextView)findViewById(R.id.numbeMedShips);
        TextView t_big = (TextView)findViewById(R.id.numberBigShips);
        switch (activeShip) {
           case 1:
               t_small.setText(String.valueOf(availableSmallShips));
                break;
            case 3:
                t_med.setText(String.valueOf(availableMedShips));
                break;
            case 5:
                t_big.setText(String.valueOf(availableBigShips));
        }
    }
   public void updateBattlefield (Ship ship, View view){
        String cordinate = ship.getAllCordinates();
        for (int i = 0; i < cordinate.length() - 1; i = i+2) {
            String tag = cordinate.substring(i,i+1);
            TextView tv = (TextView) view.findViewWithTag(tag);
            tv.setTextColor(Color.WHITE);
            //tv.setText("o");
            tv.setBackgroundResource(R.mipmap.sea_ship);
            storeTextview(tv);
        }
}
}
