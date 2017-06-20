package group5.battleship.src.views;


import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import group5.battleship.R;
import group5.battleship.src.logic.Battlefield;
import group5.battleship.src.logic.Cordinate;
import group5.battleship.src.logic.Ship;

// this activity is meant for different shiptypes, the standart gamemode should use the old version of this activity
public class SetShipsActivity2 extends AppCompatActivity {
    String ships = ""; //XYXYXY
    int MAX_SHIPS = 6;
    int currentShips = 0;
    int activeShip = 0; // 0 = undefined, 1 = small, 3 = medium, 5 = big
    String tempDirection = "v";
    ArrayList<TextView> textViews = new ArrayList<>();
    Battlefield tmpBattlefield = new Battlefield(8);
    int availableSmallShips = 3;
    int availableMedShips = 2;
    int availableBigShips = 1;
    // TODO: 03.06.2017 should get the max_ships and size of battlefield as extra
    int[][] routingMyField;
    public MediaPlayer playPutSound = new MediaPlayer();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: 03.06.2017 contentview should be received from the button in main 
        setContentView(R.layout.activity_set_ships_2);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        TextView t_small = (TextView) findViewById(R.id.numberSmallShips);
        TextView t_med = (TextView) findViewById(R.id.numbeMedShips);
        TextView t_big = (TextView) findViewById(R.id.numberBigShips);
        t_small.setText(String.valueOf(availableSmallShips));
        t_med.setText(String.valueOf(availableMedShips));
        t_big.setText(String.valueOf(availableBigShips));

        playPutSound = MediaPlayer.create(SetShipsActivity2.this, R.raw.put_sound);

        Context context = getApplicationContext();
        CharSequence text = "Click on the area to set your ships!";
        int duration = Toast.LENGTH_SHORT;

        Toast.makeText(context, text, duration).show();

        routingToTableLayout();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("my tag", getIntent().getStringExtra("NAME") + " debug");
        Log.d("my tag", "Resume in set ship");
        currentShips = 0;
        ships="";

    }

    // TODO: 03.06.2017 add a textview and a 'if-else' to the layout that dec the number of available ships of a special type after setting  
    public void chooseShip(View view) {
        ImageButton small = (ImageButton) findViewById(R.id.smallShip);
        ImageButton medium = (ImageButton) findViewById(R.id.mediumShip);
        ImageButton big = (ImageButton) findViewById(R.id.bigShip);
        TextView t_small = (TextView) findViewById(R.id.numberSmallShips);
        TextView t_med = (TextView) findViewById(R.id.numbeMedShips);
        TextView t_big = (TextView) findViewById(R.id.numberBigShips);

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        CharSequence text ="";
        activeShip = 0;
        switch (view.getId()) {
            case R.id.smallShip:
                activeShip = 1;
                text = "Small Ship selected! "+activeShip;
                break;
            case R.id.mediumShip:
                activeShip = 3;
                text = "Medium Ship selected! "+activeShip;
                break;
            case R.id.bigShip:
                text = "Big Ship selected! "+activeShip;
                activeShip = 5;
        }
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
        small.setVisibility(View.INVISIBLE);
        medium.setVisibility(View.INVISIBLE);
        big.setVisibility(View.INVISIBLE);
        t_small.setVisibility(View.INVISIBLE);
        t_med.setVisibility(View.INVISIBLE);
        t_big.setVisibility(View.INVISIBLE);

        ImageView iv = (ImageView)findViewById(R.id.imageView);
        Button b = (Button)findViewById(R.id.button2);

        iv.setVisibility(View.INVISIBLE);
        b.setVisibility(View.INVISIBLE);
    }

    public void rotate(View view) {
        CharSequence text;
        if (tempDirection.equals("v")) {
            tempDirection = "h";
            text = "Your ships will be set horizontal";
        } else {
            tempDirection = "v";
            text = "Your ships will be set vertical ";
        }
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    public void cellClick(View view) {

        if (currentShips < MAX_SHIPS) {

            Cordinate mainCordinate = new Cordinate((String) view.getTag());
            Ship ship = new Ship(activeShip, tempDirection, mainCordinate);
            if (tmpBattlefield.checkSpace(ship)) {
                tmpBattlefield.addShip(ship);
                currentShips++;
                updateBattlefield();

                ships += ship.returnAllCordinatesAsString(ship);
                Log.d("SHIPS Set ship activity", String.valueOf(ships.length()) );
                Log.d("SHIPS Set ship activity", ships );
                playPutSound.start();
                switch (activeShip) {
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
                        break;
                }
                activeShip = 0;
                ImageButton small = (ImageButton) findViewById(R.id.smallShip);
                ImageButton medium = (ImageButton) findViewById(R.id.mediumShip);
                ImageButton big = (ImageButton) findViewById(R.id.bigShip);

                TextView t_small = (TextView) findViewById(R.id.numberSmallShips);
                TextView t_med = (TextView) findViewById(R.id.numbeMedShips);
                TextView t_big = (TextView) findViewById(R.id.numberBigShips);
                if (availableSmallShips > 0) {
                    small.setVisibility(View.VISIBLE);
                    t_small.setVisibility(View.VISIBLE);
                }
                if (availableMedShips > 0) {
                    medium.setVisibility(View.VISIBLE);
                    t_med.setVisibility(View.VISIBLE);
                }
                if (availableBigShips > 0) {
                    big.setVisibility(View.VISIBLE);
                    t_big.setVisibility(View.VISIBLE);
                }
                ImageView iv = (ImageView)findViewById(R.id.imageView);
                Button b = (Button)findViewById(R.id.button2);

                iv.setVisibility(View.VISIBLE);
                b.setVisibility(View.VISIBLE);

            } else {
                Context context = getApplicationContext();
                CharSequence text = "Ship cant be set here!";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }

            if (currentShips == MAX_SHIPS) {

                ImageButton small = (ImageButton) findViewById(R.id.smallShip);
                ImageButton medium = (ImageButton) findViewById(R.id.mediumShip);
                ImageButton big = (ImageButton) findViewById(R.id.bigShip);
                TextView t_small = (TextView) findViewById(R.id.numberSmallShips);
                TextView t_med = (TextView) findViewById(R.id.numbeMedShips);
                TextView t_big = (TextView) findViewById(R.id.numberBigShips);
                small.setVisibility(View.INVISIBLE);
                medium.setVisibility(View.INVISIBLE);
                big.setVisibility(View.INVISIBLE);
                t_small.setVisibility(View.INVISIBLE);
                t_med.setVisibility(View.INVISIBLE);
                t_big.setVisibility(View.INVISIBLE);

                ImageView iv = (ImageView)findViewById(R.id.imageView);
                Button b = (Button)findViewById(R.id.button2);

                iv.setVisibility(View.INVISIBLE);
                b.setVisibility(View.INVISIBLE);


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
                new AlertDialog.Builder(SetShipsActivity2.this)
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
                        .show();*/


            } else if (currentShips == MAX_SHIPS) {


                // this code is not needed at the moment

               /* Context context = getApplicationContext();
                CharSequence text = "All ships are set";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                Button b15 = (Button) findViewById(R.id.startButton);
                b15.setVisibility(View.VISIBLE);

                View table = findViewById(R.id.table);

                table.setVisibility(View.INVISIBLE);*/


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
        intent.putExtra("Standart Mod", getIntent().getBooleanExtra("Standart Mod", false));
        intent.putExtra("Battlefield",tmpBattlefield);

        startActivity(intent);

    }

    public void showStartButton() {

        TextView acceptText = (TextView) findViewById(R.id.acceptText);
        acceptText.setVisibility(View.INVISIBLE);

        Button yesbtn = (Button) findViewById(R.id.yesbtn);
        Button nobtn = (Button) findViewById(R.id.nobtn);

        yesbtn.setVisibility(View.INVISIBLE);
        nobtn.setVisibility(View.INVISIBLE);
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
        TextView tv;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tv = (TextView) findViewById(getRoutingByCordinateMyField(new Cordinate(i, j)));
                tv.setBackgroundResource(R.mipmap.meer_neu);
                }
            }
        tmpBattlefield.resetBattlefield();

        // clear all variables
        textViews.clear();
        ships = "";
        currentShips = 0;
        availableSmallShips = 3;
        availableMedShips = 2;
        availableBigShips = 1;

        TextView t_small = (TextView) findViewById(R.id.numberSmallShips);
        TextView t_med = (TextView) findViewById(R.id.numbeMedShips);
        TextView t_big = (TextView) findViewById(R.id.numberBigShips);
        t_small.setText(String.valueOf(availableSmallShips));
        t_med.setText(String.valueOf(availableMedShips));
        t_big.setText(String.valueOf(availableBigShips));

        //recreate the initial view
        TextView setShipsText = (TextView) findViewById(R.id.setShips);
        setShipsText.setVisibility(View.VISIBLE);

        TextView acceptText = (TextView) findViewById(R.id.acceptText);
        acceptText.setVisibility(View.INVISIBLE);

        Button yesbtn = (Button) findViewById(R.id.yesbtn);
        Button nobtn = (Button) findViewById(R.id.nobtn);

        yesbtn.setVisibility(View.INVISIBLE);
        nobtn.setVisibility(View.INVISIBLE);
        ImageButton small = (ImageButton) findViewById(R.id.smallShip);
        ImageButton medium = (ImageButton) findViewById(R.id.mediumShip);
        ImageButton big = (ImageButton) findViewById(R.id.bigShip);


        small.setVisibility(View.VISIBLE);
        t_small.setVisibility(View.VISIBLE);


        medium.setVisibility(View.VISIBLE);
        t_med.setVisibility(View.VISIBLE);


        big.setVisibility(View.VISIBLE);
        t_big.setVisibility(View.VISIBLE);

        ImageView iv = (ImageView)findViewById(R.id.imageView);
        Button b = (Button)findViewById(R.id.button2);

        iv.setVisibility(View.VISIBLE);
        b.setVisibility(View.VISIBLE);

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

    public void updateTextView(int activeShip) {
        TextView t_small = (TextView) findViewById(R.id.numberSmallShips);
        TextView t_med = (TextView) findViewById(R.id.numbeMedShips);
        TextView t_big = (TextView) findViewById(R.id.numberBigShips);
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

    public void updateBattlefield() {
        TextView tv;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                tv = (TextView) findViewById(getRoutingByCordinateMyField(new Cordinate(i, j)));

                if (tmpBattlefield.getValueOfCell(new Cordinate(i,j)) == 1) {
                    //tv.setText("o");
                    tv.setBackgroundResource(R.mipmap.sea_ship);
                } else {
                    //tv.setText("");
                    tv.setBackgroundResource(R.mipmap.meer_neu);
                }
            }
        }
    }


    private void routingToTableLayout() {
        routingMyField = new int[8][8];

        routingMyField[0][0] = findViewById(R.id.textView00).getId();
        routingMyField[0][1] = findViewById(R.id.textView01).getId();
        routingMyField[0][2] = findViewById(R.id.textView02).getId();
        routingMyField[0][3] = findViewById(R.id.textView03).getId();
        routingMyField[0][4] = findViewById(R.id.textView04).getId();
        routingMyField[0][5] = findViewById(R.id.textView05).getId();
        routingMyField[0][6] = findViewById(R.id.textView06).getId();
        routingMyField[0][7] = findViewById(R.id.textView07).getId();

        routingMyField[1][0] = findViewById(R.id.textView10).getId();
        routingMyField[1][1] = findViewById(R.id.textView11).getId();
        routingMyField[1][2] = findViewById(R.id.textView12).getId();
        routingMyField[1][3] = findViewById(R.id.textView13).getId();
        routingMyField[1][4] = findViewById(R.id.textView14).getId();
        routingMyField[1][5] = findViewById(R.id.textView15).getId();
        routingMyField[1][6] = findViewById(R.id.textView16).getId();
        routingMyField[1][7] = findViewById(R.id.textView17).getId();

        routingMyField[2][0] = findViewById(R.id.textView20).getId();
        routingMyField[2][1] = findViewById(R.id.textView21).getId();
        routingMyField[2][2] = findViewById(R.id.textView22).getId();
        routingMyField[2][3] = findViewById(R.id.textView23).getId();
        routingMyField[2][4] = findViewById(R.id.textView24).getId();
        routingMyField[2][5] = findViewById(R.id.textView25).getId();
        routingMyField[2][6] = findViewById(R.id.textView26).getId();
        routingMyField[2][7] = findViewById(R.id.textView27).getId();

        routingMyField[3][0] = findViewById(R.id.textView30).getId();
        routingMyField[3][1] = findViewById(R.id.textView31).getId();
        routingMyField[3][2] = findViewById(R.id.textView32).getId();
        routingMyField[3][3] = findViewById(R.id.textView33).getId();
        routingMyField[3][4] = findViewById(R.id.textView34).getId();
        routingMyField[3][5] = findViewById(R.id.textView35).getId();
        routingMyField[3][6] = findViewById(R.id.textView36).getId();
        routingMyField[3][7] = findViewById(R.id.textView37).getId();

        routingMyField[4][0] = findViewById(R.id.textView40).getId();
        routingMyField[4][1] = findViewById(R.id.textView41).getId();
        routingMyField[4][2] = findViewById(R.id.textView42).getId();
        routingMyField[4][3] = findViewById(R.id.textView43).getId();
        routingMyField[4][4] = findViewById(R.id.textView44).getId();
        routingMyField[4][5] = findViewById(R.id.textView45).getId();
        routingMyField[4][6] = findViewById(R.id.textView46).getId();
        routingMyField[4][7] = findViewById(R.id.textView47).getId();

        routingMyField[5][0] = findViewById(R.id.textView50).getId();
        routingMyField[5][1] = findViewById(R.id.textView51).getId();
        routingMyField[5][2] = findViewById(R.id.textView52).getId();
        routingMyField[5][3] = findViewById(R.id.textView53).getId();
        routingMyField[5][4] = findViewById(R.id.textView54).getId();
        routingMyField[5][5] = findViewById(R.id.textView55).getId();
        routingMyField[5][6] = findViewById(R.id.textView56).getId();
        routingMyField[5][7] = findViewById(R.id.textView57).getId();

        routingMyField[6][0] = findViewById(R.id.textView60).getId();
        routingMyField[6][1] = findViewById(R.id.textView61).getId();
        routingMyField[6][2] = findViewById(R.id.textView62).getId();
        routingMyField[6][3] = findViewById(R.id.textView63).getId();
        routingMyField[6][4] = findViewById(R.id.textView64).getId();
        routingMyField[6][5] = findViewById(R.id.textView65).getId();
        routingMyField[6][6] = findViewById(R.id.textView66).getId();
        routingMyField[6][7] = findViewById(R.id.textView67).getId();

        routingMyField[7][0] = findViewById(R.id.textView70).getId();
        routingMyField[7][1] = findViewById(R.id.textView71).getId();
        routingMyField[7][2] = findViewById(R.id.textView72).getId();
        routingMyField[7][3] = findViewById(R.id.textView73).getId();
        routingMyField[7][4] = findViewById(R.id.textView74).getId();
        routingMyField[7][5] = findViewById(R.id.textView75).getId();
        routingMyField[7][6] = findViewById(R.id.textView76).getId();
        routingMyField[7][7] = findViewById(R.id.textView77).getId();


    }


    private int getRoutingByCordinateMyField(Cordinate c) {
        return routingMyField[c.x][c.y];
    }

}
