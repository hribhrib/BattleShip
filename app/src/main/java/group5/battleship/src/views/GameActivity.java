package group5.battleship.src.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.IntegerRes;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;

import java.util.Random;

import group5.battleship.R;
import group5.battleship.src.logic.Cordinate;
import group5.battleship.src.logic.Game;
import group5.battleship.src.logic.Move;
import group5.battleship.src.logic.Player;
import group5.battleship.src.logic.ShakeDetector;

import android.widget.Button;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import group5.battleship.src.wifi.ClientThread;
import group5.battleship.src.wifi.ServerThread;
import group5.battleship.src.logic.randomShipCordinate;
import group5.battleship.src.logic.randomWaterCordinate;


public class GameActivity extends AppCompatActivity {
    public Game game;
    private Player myPlayer;
    private Player opponent;
    int[][] routingMyField;
    int[][] routingOpponentField;
    TabHost tapHost;
    // for shakeDetection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    //soundfiles
    public MediaPlayer playShootingSound;
    public MediaPlayer playMissSound;
    public MediaPlayer playHitSound;
    public MediaPlayer playGameSound;
    public MediaPlayer playWinSound;
    public MediaPlayer playLoseSound;

    int tempRoundCount = 0;



    TextView tv;
    boolean firebtnpressed = false;


    /////////////////////////////////////////

    ClientThread clientThread;
    ServerThread serverThread;
    InetAddress hostAddress;
    String stringHostAddress;

    Timer myTimer;
    TimerTask myTask;

    Intent intent;

    Boolean host;
    Boolean gameEnd = false;
    static String send = "";

    AlertDialog waitDialog;
    int port = 8888;
    boolean oppReady = false;
    String oppShips = "";
    String oppMove;


    //////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tapHost = (TabHost) findViewById(R.id.tabHost);
        tapHost.setup();

        //Tab 1
        TabHost.TabSpec spec = tapHost.newTabSpec("MyField");
        spec.setContent(R.id.MyField);
        spec.setIndicator("MyField");
        tapHost.addTab(spec);

        //Tab 2
        spec = tapHost.newTabSpec("OpponentField");
        spec.setContent(R.id.OpponentField);
        spec.setIndicator("OpponentField");
        tapHost.addTab(spec);

        playShootingSound = MediaPlayer.create(GameActivity.this, R.raw.shooting);
        playHitSound = MediaPlayer.create(GameActivity.this, R.raw.ship_hit);
        playMissSound = MediaPlayer.create(GameActivity.this, R.raw.water_hit);
        playWinSound = MediaPlayer.create(GameActivity.this, R.raw.win);
        playGameSound = MediaPlayer.create(GameActivity.this, R.raw.battlemusic);
        playLoseSound = MediaPlayer.create(GameActivity.this, R.raw.lose);

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
                randomAttack(count);
            }
        });


        intent = getIntent();
        //if this is a wifi game, start Client/Server Thread
        if (intent.getBooleanExtra("WIFI", true)) {

            Log.d("My Log", "WIFI TRUE");
            //Check for the connection
            if (intent.getBooleanExtra("Connected", true)) {
                //Get hostaddress as string
                stringHostAddress = intent.getStringExtra("HostAddress");

                //Convert that to Inet Address
                try {
                    hostAddress = InetAddress.getByName(stringHostAddress);

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }

            //Determine if this user is host or client
            host = intent.getBooleanExtra("IsHost", true);
            Log.d("My Log", "Is HOST?!" + String.valueOf(host));
            //the Host runs the Server-Thread, Client runs the Client-Thread
            if (host) {

                serverThread = new ServerThread(port);
                new Thread(serverThread).start();
                waitDialog = new AlertDialog.Builder(GameActivity.this).create();
                waitDialog.setMessage(getString(R.string.message_wait_for_attack));
                waitDialog.setCancelable(false);
                waitDialog.setCanceledOnTouchOutside(false);
                waitDialog.show();
            } else {
                clientThread = new ClientThread(hostAddress, port);
                new Thread(clientThread).start();
            }

            initGame();
        } else {
            initGame();
            initDummyOpp();
            displayOpponentsBattleField();
            displayMyBattleField();
        }


    }

    public void onResume() {
        playGameSound.start();
        playGameSound.setLooping(true);
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);


        //The hostdevice will display host for player 1, and upon receiving the packets from client
        //Client will display for player 2
        //Receice automaticly
        if (intent.getBooleanExtra("WIFI", true)) {
            myTimer = new Timer();
            host = getIntent().getBooleanExtra("IsHost", true);
            myTask = new TimerTask() {
                @Override
                public void run() {
                    if (host) {
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                //Got opp ships, only first time
                                if (serverThread.getPlayer2String() != null && !oppReady) {
                                    oppReady = true;
                                    oppMove = serverThread.getPlayer2String();
                                    oppShips = serverThread.getPlayer2String();
                                    initRealOpp();
                                    displayOpponentsBattleField();
                                    displayMyBattleField();
                                }
                                //All other cycles, find new move
                                else if (serverThread.getPlayer2String() != null &&
                                        !serverThread.getPlayer2String().equals(oppMove)) {
                                    oppMove = serverThread.getPlayer2String();
                                    realOpponentsMove();
                                    if (waitDialog != null && !gameEnd) {
                                        waitDialog.dismiss();
                                    }

                                }
                            }
                        };
                        runOnUiThread(runnable);
                    } else if (!host) {
                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                //Got opp ships, only first time
                                if (clientThread.getPlayer2String() != null && !oppReady) {
                                    oppReady = true;
                                    oppMove = clientThread.getPlayer2String();
                                    oppShips = clientThread.getPlayer2String();
                                    initRealOpp();
                                    displayOpponentsBattleField();
                                    displayMyBattleField();

                                }
                                //All other cycles, find new move
                                else if (clientThread.getPlayer2String() != null &&
                                        !clientThread.getPlayer2String().equals(oppMove)) {
                                    oppMove = clientThread.getPlayer2String();
                                    realOpponentsMove();
                                    if (waitDialog != null && !gameEnd) {
                                        waitDialog.dismiss();
                                    }
                                }
                            }
                        };
                        runOnUiThread(runnable);
                    }
                }
            };
            myTimer.schedule(myTask, 10, 10);
        }
    }

    public void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
        playGameSound.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    public void playBattleSound(){
        playGameSound.setLooping(true);
        playGameSound.start();
    }

    public void cellClick(View view) {

        final Button firebtn = (Button) findViewById(R.id.firebtn);

        if (firebtn.getVisibility() == View.VISIBLE) {
            // reset the view before setting the new target
            displayMyBattleField();
        }

        tv = (TextView) findViewById(view.getId());
        tv.setBackgroundResource(R.mipmap.crosshair_sea);


        // press fire Button
        firebtn.setVisibility(View.VISIBLE);
        firebtnpressed = false;
        firebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shotCell(tv);
            }
        });

    }

    public void shotCell(TextView tv) {

        Cordinate c = getRoutingByIDOpponentField(tv.getId());
        if (myPlayer.getBattleFieldByCordinate(c) == 0) {

            int[][] tmpOpponentShips = opponent.getShips();

            if (tmpOpponentShips[c.x][c.y] == -1) {

                myPlayer.updateBattleField(c.x, c.y, -1);
                playHitSound.start();
            } else if (tmpOpponentShips[c.x][c.y] == 1) {
                playMissSound.start();
                myPlayer.updateBattleField(c.x, c.y, 1);
                if (opponent.incShipDestroyed() == opponent.getMaxShips()) {
                    endGame(myPlayer);
                }
            }

            game.newMove(new Move(myPlayer, opponent, c));
            displayMyBattleField();
            tempRoundCount++;

            if (intent.getBooleanExtra("WIFI", true)) {

                send = String.valueOf(c.x) + String.valueOf(c.y);
                host = getIntent().getBooleanExtra("IsHost", true);
                if (host) {
                    serverThread.dataReady(send);
                } else if (!host) {
                    clientThread.dataReady(send);

                }
                if (!gameEnd) {
                    waitDialog = new AlertDialog.Builder(GameActivity.this).create();
                    waitDialog.setMessage(getString(R.string.message_wait_for_counterattack));
                    waitDialog.setCancelable(false);
                    waitDialog.setCanceledOnTouchOutside(false);
                    waitDialog.show();
                }

            } else {
                /*
                waitDialog = new AlertDialog.Builder(GameActivity.this).create();
                waitDialog.setMessage("Wait for the counter attack...");
                waitDialog.setCancelable(false);
                waitDialog.setCanceledOnTouchOutside(false);
                waitDialog.show();*/
                aiOpponentsMove();
            }
        }

        //set fire button invisible agian
        Button firebtn = (Button) findViewById(R.id.firebtn);
        firebtn.setVisibility(View.INVISIBLE);


    }

    private void endGame(final Player winner) {
        playGameSound.stop();
        if (winner .equals(myPlayer) ) {
        playWinSound.start();
        updateStats(this,true);
        } else {
            playLoseSound.start();
            updateStats(this,false);
        }

        Log.d("My LOG", winner.getName() + " deb");
        Log.d("My LOG", myPlayer.getName() + " ich");
        Log.d("My LOG", opponent.getName() + " er");
        if (waitDialog != null) {
            waitDialog.dismiss();
        }

        waitDialog = new AlertDialog.Builder(GameActivity.this).create();
        waitDialog.setTitle(getString(R.string.title_game_end));

        if (!intent.getBooleanExtra("WIFI", true)) {
            waitDialog.setMessage(getString(R.string.string_fragment_player) + winner.getName() + " " + getString(R.string.string_fragment_won) + " " + getString(R.string.message_play_one_more));
            waitDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.button_yes),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Set your ships on an other position
                            startAgain();

                        }
                    });
            waitDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.button_no),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //go to homescreen
                            toStartScreen();
                        }
                    });
        } else {
            //playLoseSound.start();
            waitDialog.setMessage(getString(R.string.string_fragment_player) + winner.getName() + getString(R.string.string_fragment_won));
            waitDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.button_ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //go to homescreen
                            toStartScreen();

                        }
                    });
        }

        waitDialog.setIcon(android.R.drawable.ic_dialog_alert);
        waitDialog.setCancelable(false);
        waitDialog.setCanceledOnTouchOutside(false);
        waitDialog.show();

        gameEnd = true;

    }

    private void startAgain() {
        Intent restart = new Intent(this, SetShipsActivity.class);
        Log.d("MY LOG", intent.getStringExtra("NAME"));
        restart.putExtra("NAME", intent.getStringExtra("NAME"));
        startActivity(restart);
    }

    private void toStartScreen() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void initGame() {
        myPlayer = new Player(getIntent().getStringExtra("NAME"));
        opponent = new Player("Opponent");
        game = new Game(myPlayer, opponent, 5); //5 = static size
        playBattleSound();
        myPlayer.setShips(getIntent().getStringExtra("SHIPS"));


        //On button click the coordinates get send to opp
        AlertDialog alertDialog = new AlertDialog.Builder(GameActivity.this).create();
        alertDialog.setMessage(getString(R.string.message_are_you_rdy));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.button_yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (intent.getBooleanExtra("WIFI", true)) {
                            host = getIntent().getBooleanExtra("IsHost", true);
                            send = getIntent().getStringExtra("SHIPS");
                            if (host) {
                                Log.d("My Log", "send" + send);
                                serverThread.dataReady(send);
                            } else if (!host) {
                                Log.d("My Log", "send" + send);
                                clientThread.dataReady(send);

                            }
                        }
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

        Log.d("My Log", "init Game nach Dialog");

        routingToTableLayout();


    }

    private void initDummyOpp() {
        Random r = new Random();
        Cordinate ship1, ship2, ship3;

        ship1 = new Cordinate(r.nextInt(5), r.nextInt(5));

        do {
            ship2 = new Cordinate(r.nextInt(5), r.nextInt(5));
        } while (ship1.equals(ship2) == true);

        do {
            ship3 = new Cordinate(r.nextInt(5), r.nextInt(5));
        } while (ship1.equals(ship3) == true && ship2.equals(ship3) == true);


        opponent.setShips(ship1, ship2, ship3);
    }

    private void initRealOpp() {
        Cordinate ship1, ship2, ship3;
        Log.d("My Log", "HOST DEB" + String.valueOf(host));
        Log.d("My Log", "oppShips String" + oppShips);


        ship1 = new Cordinate(Character.getNumericValue(oppShips.charAt(0)),
                Character.getNumericValue(oppShips.charAt(1)));
        ship2 = new Cordinate(Character.getNumericValue(oppShips.charAt(2)),
                Character.getNumericValue(oppShips.charAt(3)));
        ship3 = new Cordinate(Character.getNumericValue(oppShips.charAt(4)),
                Character.getNumericValue(oppShips.charAt(5)));


        opponent.setShips(ship1, ship2, ship3);

        Log.d("My Log", "Opponents Ships: " + oppShips);
    }

    private void aiOpponentsMove() {
        tapHost.setCurrentTab(0);

        Random r = new Random();
        Cordinate c;

        do {
            c = new Cordinate(r.nextInt(5), r.nextInt(5));
        } while (opponent.getBattleFieldByCordinate(c) != 0);

        int[][] tmpMyShips = myPlayer.getShips();

        if (tmpMyShips[c.x][c.y] == -1) {
            opponent.updateBattleField(c.x, c.y, -1);
        } else if (tmpMyShips[c.x][c.y] == 1) {
            phoneVibrate();
            opponent.updateBattleField(c.x, c.y, 1);
            if (myPlayer.incShipDestroyed() == myPlayer.getMaxShips()) {
                endGame(opponent);
            }
        }

        displayOpponentsBattleField();

        //display Opponents shot
        Context context = getApplicationContext();
        CharSequence text = c.x + "" + c.y;
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, text, duration).show();

        game.newMove(new Move(opponent, myPlayer, c));
        //waitDialog.dismiss();
    }

    private void realOpponentsMove() {
        tapHost.setCurrentTab(0);

        Cordinate c = new Cordinate((int) oppMove.charAt(0) - 48, (int) oppMove.charAt(1) - 48);

        int[][] tmpMyShips = myPlayer.getShips();

        if (tmpMyShips[c.x][c.y] == -1) {
            playMissSound.start();
            opponent.updateBattleField(c.x, c.y, -1);
        } else if (tmpMyShips[c.x][c.y] == 1) {
            playHitSound.start();
            phoneVibrate();
            opponent.updateBattleField(c.x, c.y, 1);
            if (myPlayer.incShipDestroyed() == myPlayer.getMaxShips()) {
                endGame(opponent);
            }
        }

        displayOpponentsBattleField();


        //display Opponents shot
        Context context = getApplicationContext();
        CharSequence text = c.x + "" + c.y;
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, text, duration).show();

        game.newMove(new Move(opponent, myPlayer, c));
    }

    private void displayMyShips() {
        int[][] ships = myPlayer.getShips();

        TextView tv;
        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                tv = (TextView) findViewById(getRoutingByCordinateMyField(i, j));
                tv.setTextSize(20);
                tv.setTextColor(Color.WHITE);
                if (ships[i][j] == 1) {
                    tv.setText("o");
                } else {
                    tv.setText("~");
                }
            }
        }
    }

    private void displayOpponentsBattleField() {

        int[][] opBattleField = opponent.getBattleField();

        int[][] myShips = myPlayer.getShips();

        TextView tv;
        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                tv = (TextView) findViewById(getRoutingByCordinateMyField(i, j));
                tv.setTextSize(20);
                tv.setTextColor(Color.WHITE);
                if (opBattleField[i][j] == 1) {
                    tv.setBackgroundResource(R.mipmap.sea_ship_destroyed);
                } else if (opBattleField[i][j] == -1) {
                    tv.setBackgroundResource(R.mipmap.sea_wronghit);
                } else {
                    // set the other ships so that you see where your ships are
                    if (myShips[i][j] == 1) {
                        tv.setBackgroundResource(R.mipmap.sea_ship);
                    } else {
                        tv.setBackgroundResource(R.mipmap.meer_neu);
                    }
                }
            }
        }
    }

    private void displayinitialOpponentsBattleField() {

        opponent.setBattleField(opponent.getShips());
        int[][] opBattleField = opponent.getBattleField();

        TextView tv;
        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                tv = (TextView) findViewById(getRoutingByCordinateMyField(i, j));
                if (opBattleField[i][j] == 1) {
                    tv.setBackgroundResource(R.mipmap.sea_ship);
                } else if (opBattleField[i][j] == -1) {
                    tv.setBackgroundResource(R.mipmap.meer_neu);
                } else {
                    tv.setBackgroundResource(R.mipmap.meer_neu);
                }
            }
        }
    }

    private void displayMyBattleField() {
        int[][] battleField = myPlayer.getBattleField();

        TextView tv;
        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                tv = (TextView) findViewById(getRoutingByCordinateOpponentField(i, j));
                tv.setTextSize(20);
                tv.setTextColor(Color.WHITE);
                if (battleField[i][j] == 1) {
                    //tv.setText("o");
                    tv.setBackgroundResource(R.mipmap.sea_ship_destroyed);
                } else if (battleField[i][j] == -1) {
                    //tv.setText("~");
                    tv.setBackgroundResource(R.mipmap.sea_wronghit);
                } else {
                    //tv.setText("");
                    tv.setBackgroundResource(R.mipmap.meer_neu);
                }
            }
        }
    }

    private void routingToTableLayout() {
        routingMyField = new int[game.getSize()][game.getSize()];

        routingMyField[0][0] = findViewById(R.id.textView00).getId();
        routingMyField[0][1] = findViewById(R.id.textView01).getId();
        routingMyField[0][2] = findViewById(R.id.textView02).getId();
        routingMyField[0][3] = findViewById(R.id.textView03).getId();
        routingMyField[0][4] = findViewById(R.id.textView04).getId();
        routingMyField[1][0] = findViewById(R.id.textView10).getId();
        routingMyField[1][1] = findViewById(R.id.textView11).getId();
        routingMyField[1][2] = findViewById(R.id.textView12).getId();
        routingMyField[1][3] = findViewById(R.id.textView13).getId();
        routingMyField[1][4] = findViewById(R.id.textView14).getId();
        routingMyField[2][0] = findViewById(R.id.textView20).getId();
        routingMyField[2][1] = findViewById(R.id.textView21).getId();
        routingMyField[2][2] = findViewById(R.id.textView22).getId();
        routingMyField[2][3] = findViewById(R.id.textView23).getId();
        routingMyField[2][4] = findViewById(R.id.textView24).getId();
        routingMyField[3][0] = findViewById(R.id.textView30).getId();
        routingMyField[3][1] = findViewById(R.id.textView31).getId();
        routingMyField[3][2] = findViewById(R.id.textView32).getId();
        routingMyField[3][3] = findViewById(R.id.textView33).getId();
        routingMyField[3][4] = findViewById(R.id.textView34).getId();
        routingMyField[4][0] = findViewById(R.id.textView40).getId();
        routingMyField[4][1] = findViewById(R.id.textView41).getId();
        routingMyField[4][2] = findViewById(R.id.textView42).getId();
        routingMyField[4][3] = findViewById(R.id.textView43).getId();
        routingMyField[4][4] = findViewById(R.id.textView44).getId();

        routingOpponentField = new int[game.getSize()][game.getSize()];

        routingOpponentField[0][0] = findViewById(R.id.opponentTextView00).getId();
        routingOpponentField[0][1] = findViewById(R.id.opponentTextView01).getId();
        routingOpponentField[0][2] = findViewById(R.id.opponentTextView02).getId();
        routingOpponentField[0][3] = findViewById(R.id.opponentTextView03).getId();
        routingOpponentField[0][4] = findViewById(R.id.opponentTextView04).getId();
        routingOpponentField[1][0] = findViewById(R.id.opponentTextView10).getId();
        routingOpponentField[1][1] = findViewById(R.id.opponentTextView11).getId();
        routingOpponentField[1][2] = findViewById(R.id.opponentTextView12).getId();
        routingOpponentField[1][3] = findViewById(R.id.opponentTextView13).getId();
        routingOpponentField[1][4] = findViewById(R.id.opponentTextView14).getId();
        routingOpponentField[2][0] = findViewById(R.id.opponentTextView20).getId();
        routingOpponentField[2][1] = findViewById(R.id.opponentTextView21).getId();
        routingOpponentField[2][2] = findViewById(R.id.opponentTextView22).getId();
        routingOpponentField[2][3] = findViewById(R.id.opponentTextView23).getId();
        routingOpponentField[2][4] = findViewById(R.id.opponentTextView24).getId();
        routingOpponentField[3][0] = findViewById(R.id.opponentTextView30).getId();
        routingOpponentField[3][1] = findViewById(R.id.opponentTextView31).getId();
        routingOpponentField[3][2] = findViewById(R.id.opponentTextView32).getId();
        routingOpponentField[3][3] = findViewById(R.id.opponentTextView33).getId();
        routingOpponentField[3][4] = findViewById(R.id.opponentTextView34).getId();
        routingOpponentField[4][0] = findViewById(R.id.opponentTextView40).getId();
        routingOpponentField[4][1] = findViewById(R.id.opponentTextView41).getId();
        routingOpponentField[4][2] = findViewById(R.id.opponentTextView42).getId();
        routingOpponentField[4][3] = findViewById(R.id.opponentTextView43).getId();
        routingOpponentField[4][4] = findViewById(R.id.opponentTextView44).getId();
    }

    private int getRoutingByCordinateMyField(int x, int y) {
        return routingMyField[x][y];
    }

    private int getRoutingByCordinateOpponentField(int x, int y) {
        return routingOpponentField[x][y];
    }

    private Cordinate getRoutingByIDOpponentField(int id) {
        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                if (routingOpponentField[i][j] == id) {
                    return new Cordinate(i, j);
                }
            }
        }
        return new Cordinate(-1, -1);
    }

    private void playSoundHitShip() {

    }

    private void phoneVibrate() {
        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(300); //Vibration 300 milisekunden
    }

    private void radar(Cordinate c) {

    }

    private void printInfo() {
        Toast.makeText(getBaseContext(), getString(R.string.toast_remaining_randomAttacks) + myPlayer.getRandomAttacks(),
                Toast.LENGTH_LONG).show();
    }

    public void randomAttack(int count) {
        if (myPlayer.getRandomAttacks() > 0) {
            tempRoundCount++;
            Cordinate randomShipCordinate = new randomShipCordinate(opponent, game);
            Cordinate randomWaterCordinate = new randomWaterCordinate(opponent);
            Random r = new Random();

            if (r.nextInt(10) >= 4) {

                myPlayer.updateBattleField(randomShipCordinate, 1);
                playHitSound.start();
                if (opponent.incShipDestroyed() == opponent.getMaxShips()) {
                    endGame(myPlayer);
                }
                myPlayer.setRandomAttacks();
                game.newMove(new Move(myPlayer, opponent, randomShipCordinate));
                displayMyBattleField();
                printInfo();
                if (intent.getBooleanExtra("WIFI", true)) {

                    send = String.valueOf(randomShipCordinate.x) + String.valueOf(randomShipCordinate.y);
                    host = getIntent().getBooleanExtra("IsHost", true);
                    if (host) {
                        serverThread.dataReady(send);
                    } else if (!host) {
                        clientThread.dataReady(send);

                    }
                    if (!gameEnd) {
                        waitDialog = new AlertDialog.Builder(GameActivity.this).create();
                        waitDialog.setMessage(getString(R.string.message_wait_for_counterattack));
                        waitDialog.setCancelable(false);
                        waitDialog.setCanceledOnTouchOutside(false);
                        waitDialog.show();
                    }

                } else {
                    aiOpponentsMove();
                }

            } else {
                myPlayer.updateBattleField(randomWaterCordinate, -1);
                playMissSound.start();
                if (opponent.incShipDestroyed() == opponent.getMaxShips()) {
                    endGame(myPlayer);
                }
                myPlayer.setRandomAttacks();
                game.newMove(new Move(myPlayer, opponent, randomWaterCordinate));
                displayMyBattleField();
                printInfo();
                if (intent.getBooleanExtra("WIFI", true)) {

                    send = String.valueOf(randomWaterCordinate.x) + String.valueOf(randomWaterCordinate.y);
                    host = getIntent().getBooleanExtra("IsHost", true);
                    if (host) {
                        serverThread.dataReady(send);
                    } else if (!host) {
                        clientThread.dataReady(send);

                    }
                    if (!gameEnd) {
                        waitDialog = new AlertDialog.Builder(GameActivity.this).create();
                        waitDialog.setMessage(getString(R.string.message_wait_for_counterattack));
                        waitDialog.setCancelable(false);
                        waitDialog.setCanceledOnTouchOutside(false);
                        waitDialog.show();
                    }

                }
                else {
                    aiOpponentsMove();
                }
            }

        } else {
            printInfo();
            displayMyBattleField();
            aiOpponentsMove();

        }
    }
    public void updateStats (Context context, boolean win){
        //for storing gamestatistics

        SharedPreferences prefs = this.getSharedPreferences("stats",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = prefs.edit();

        int gameCount = prefs.getInt("totalGamesPlayed",0);
        int winCount = prefs.getInt("totalGamesWon",0);
        int loseCount = prefs.getInt("totalGamesLost",0);
        int roundCount = prefs.getInt("shortestGame",0);

        editor.putInt("totalGamesPlayed",gameCount+1);
        if (win){
            editor.putInt("totalGamesWon",winCount+1);
        } else {
            editor.putInt("totalGamesLost",loseCount+1);
        }
        if(tempRoundCount<roundCount&&win){
        editor.putInt("shortestGame",tempRoundCount);}

        editor.apply();
    }

}





