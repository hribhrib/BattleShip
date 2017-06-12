package group5.battleship.src.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import group5.battleship.R;
import group5.battleship.src.logic.Battlefield;
import group5.battleship.src.logic.Cordinate;
import group5.battleship.src.logic.DummyOppShip;
import group5.battleship.src.logic.Game;
import group5.battleship.src.logic.Move;
import group5.battleship.src.logic.Player;
import group5.battleship.src.logic.ShakeDetector;

import android.widget.Button;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import group5.battleship.src.logic.Ship;
import group5.battleship.src.wifi.ClientThread;
import group5.battleship.src.wifi.ServerThread;
import group5.battleship.src.logic.randomShipCordinate;
import group5.battleship.src.logic.randomWaterCordinate;


public class GameActivity2 extends AppCompatActivity {
    public Game game;
    private Player myPlayer;
    private Player opponent;
    int[][] routingMyField;
    int[][] routingOpponentField;
    TabHost tabHost;
    boolean touchable = true;

    // for shakeDetection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

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
    boolean hitHomeButton = true;
    String oppShips = "";
    String oppMove;
    Handler handlerAi = new Handler();
    AlertDialog alertDialog = null;
    int counter = 0;


    //soundfiles
    public MediaPlayer playShootingSound;
    public MediaPlayer playMissSound;
    public MediaPlayer playHitSound;
    public MediaPlayer playGameSound;
    public MediaPlayer playWinSound;
    public MediaPlayer playLoseSound;



    int tempRoundCount = 0;
    //////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("MY LOG", "CREATE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        playHitSound = MediaPlayer.create(GameActivity2.this, R.raw.ship_hit);
        playMissSound = MediaPlayer.create(GameActivity2.this, R.raw.water_hit);
        playWinSound = MediaPlayer.create(GameActivity2.this, R.raw.win);
        playGameSound = MediaPlayer.create(GameActivity2.this, R.raw.battlemusic);
        playLoseSound = MediaPlayer.create(GameActivity2.this, R.raw.lose);


        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec("MyField");
        spec.setContent(R.id.MyField);
        spec.setIndicator("MyField");
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec("OpponentField");
        spec.setContent(R.id.OpponentField);
        spec.setIndicator("OpponentField");
        tabHost.addTab(spec);

        tabHost.setCurrentTab(1);

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
                waitDialog = new AlertDialog.Builder(GameActivity2.this).create();
                waitDialog.setMessage("Wait for the attack...");
                waitDialog.setCancelable(false);
                waitDialog.setCanceledOnTouchOutside(false);
                waitDialog.show();
                Log.d("My Log", "ServerThread started");
            } else {
                clientThread = new ClientThread(hostAddress, port);
                new Thread(clientThread).start();
                Log.d("My Log", "ClientThread started");
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

        Log.d("MY LOG", "RESUME");

        super.onResume();
        hitHomeButton = true; //boolean to cut connection by pressing homebutton
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
                                if (serverThread != null &&
                                        serverThread.getPlayer2String() != null && !oppReady) {
                                    oppReady = true;
                                    oppMove = serverThread.getPlayer2String();
                                    oppShips = serverThread.getPlayer2String();
                                    alertDialog.dismiss();
                                    initRealOpp();
                                    displayOpponentsBattleField();
                                    displayMyBattleField();

                                }
                                //All other cycles, find new move
                                else if (serverThread != null && serverThread.getPlayer2String() != null
                                        && !serverThread.getPlayer2String().equals(oppMove)) {
                                    oppMove = serverThread.getPlayer2String();

                                    toggleWindowTouchable();

                                    //waiting.run(tabHost,0);
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            tabHost.setCurrentTab(0);
                                        }
                                    }, 1200);

                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            realOpponentsMove();
                                        }
                                    }, 2500);

                                    //realOpponentsMove();

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
                                if (clientThread != null &&
                                        clientThread.getPlayer2String() != null && !oppReady) {
                                    oppReady = true;
                                    oppMove = clientThread.getPlayer2String();
                                    oppShips = clientThread.getPlayer2String();
                                    initRealOpp();
                                    displayOpponentsBattleField();
                                    displayMyBattleField();

                                }
                                //All other cycles, find new move
                                else if (clientThread != null && clientThread.getPlayer2String() != null
                                        && !clientThread.getPlayer2String().equals(oppMove)) {
                                    oppMove = clientThread.getPlayer2String();

                                    toggleWindowTouchable();

                                    //waiting.run(tabHost,0);
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            tabHost.setCurrentTab(0);
                                        }
                                    }, 1200);

                                    handler.postDelayed(new Runnable() {
                                        public void run() {
                                            realOpponentsMove();
                                        }
                                    }, 2500);


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
        super.onPause();
        playGameSound.stop();
        mSensorManager.unregisterListener(mShakeDetector);
    }

    public void playBattleSound(){
        playGameSound.setLooping(true);
        playGameSound.start();
    }

    @Override
    public void onDestroy() {
        Log.d("My Log", "OnDestroy");
        super.onDestroy();
        if (intent.getBooleanExtra("WIFI", false)) {
            //Disconnect

            //Removes device from WifiP2pGroup
            WifiManagerActivity wm = new WifiManagerActivity();
            wm.disconnect();

            host = getIntent().getBooleanExtra("IsHost", true);
            if (host && serverThread != null) {
                serverThread.close();
                serverThread = null;
            } else if (clientThread != null) {
                clientThread.close();
                clientThread = null;
            }
            android.os.Process.killProcess(android.os.Process.myPid());
        }
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

        firebtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                counter++;
                if (counter >= 5) {
                    Toast.makeText(getBaseContext(), oppShips,
                            Toast.LENGTH_LONG).show();
                }

                return false;
            }
        });
    }


    public void shotCell(TextView tv) {

        Cordinate c = getRoutingByIDOpponentField(tv.getId());

        if (myPlayer.getBattleFieldByCordinate(c) == 0) {

            int[][] tmpOpponentShips = opponent.getShips();

            if (tmpOpponentShips[c.x][c.y] == -1) {

                myPlayer.updateBattleField(c, -1);
                playMissSound.start();

            } else if (tmpOpponentShips[c.x][c.y] == 1) {
                playHitSound.start();
                myPlayer.updateBattleField(c, 1);
                if (opponent.incShipDestroyed() == opponent.getMaxShips()) {
                    endGame(myPlayer);
                }
            }

            game.newMove(new Move(myPlayer, opponent, c));
            displayMyBattleField();
            tempRoundCount++;

            if (!intent.getBooleanExtra("WIFI", false)) {
                toggleWindowTouchable();

                //waiting.run(tabHost,0);

                handlerAi.postDelayed(new Runnable() {
                    public void run() {
                        tabHost.setCurrentTab(0);
                    }
                }, 1200);

            }


            if (intent.getBooleanExtra("WIFI", true)) {

                send = String.valueOf(c.x) + String.valueOf(c.y);
                host = getIntent().getBooleanExtra("IsHost", true);
                if (host) {
                    serverThread.dataReady(send);
                } else if (!host) {
                    clientThread.dataReady(send);

                }
                if (!gameEnd) {
                    waitDialog = new AlertDialog.Builder(GameActivity2.this).create();
                    waitDialog.setMessage("Wait for the counter attack...");
                    waitDialog.setCancelable(false);
                    waitDialog.setCanceledOnTouchOutside(false);
                    waitDialog.show();
                }

            } else {
                // Handler handler = new Handler();
                handlerAi.postDelayed(new Runnable() {
                    public void run() {
                        aiOpponentsMove();
                    }
                }, 2500);
            }
        }


        //set fire button invisible agian
        Button firebtn = (Button) findViewById(R.id.firebtn);
        firebtn.setVisibility(View.INVISIBLE);


    }

    private void endGame(final Player winner) {

        playGameSound.stop();
        if (winner.equals(myPlayer) ) {
            playWinSound.start();
            updateStats(this,true);
        } else {
            playLoseSound.start();
            updateStats(this,false);
        }

        Log.d("My LOG", winner.getName() + " winner");
        Log.d("My LOG", myPlayer.getName() + " me");
        Log.d("My LOG", opponent.getName() + " he");
        if (waitDialog != null) {
            waitDialog.dismiss();
        }

        waitDialog = new AlertDialog.Builder(GameActivity2.this).create();
        waitDialog.setTitle("GAME END!");
        waitDialog.setMessage("Player: " + winner.getName() + " won!\nWant to play a new one?");
        waitDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes!",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Disconnect
                        if (intent.getBooleanExtra("WIFI", false)) {
                            if (host) {
                                serverThread.close();
                                serverThread = null;
                            } else {
                                clientThread.close();
                                clientThread = null;
                            }
                        }
                        hitHomeButton = false;
                        //Set your ships on an other position
                        startAgain();
                    }
                });
        waitDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //go to homescreen
                        hitHomeButton = false;
                        toStartScreen();
                    }
                });
        waitDialog.setIcon(android.R.drawable.ic_dialog_alert);
        waitDialog.setCancelable(false);
        waitDialog.setCanceledOnTouchOutside(false);
        waitDialog.show();


        gameEnd = true;

    }

    private void startAgain() {
        Intent restart = new Intent(this, SetShipsActivity2.class);
        Log.d("MY LOG", intent.getStringExtra("NAME"));
        restart.putExtra("NAME", intent.getStringExtra("NAME"));
        restart.putExtra("WIFI", getIntent().getBooleanExtra("WIFI", false));
        restart.putExtra("IsHost", getIntent().getBooleanExtra("IsHost", false));
        restart.putExtra("HostAddress", intent.getStringExtra("HostAddress"));
        startActivity(restart);
    }

    private void toStartScreen() {
        //Disconnect
        if (intent.getBooleanExtra("WIFI", false)) {


            //Removes device from WifiP2pGroup
            WifiManagerActivity wm = new WifiManagerActivity();
            wm.disconnect();
            if (host) {
                serverThread.close();
                serverThread = null;
            } else {
                clientThread.close();
                clientThread = null;
            }


        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void initGame() {
        myPlayer = new Player(getIntent().getStringExtra("NAME"),8);
        opponent = new Player("Opponent",8);
        game = new Game(myPlayer, opponent, 8);
        playBattleSound();
        Battlefield passedBattlefield = (Battlefield) getIntent().getSerializableExtra("Battlefield");
        myPlayer.setShips(passedBattlefield);

        startCounter();

/*
        //Startbutton
        //On button click the coordinates get send to opp
        AlertDialog alertDialog = new AlertDialog.Builder(GameActivity2.this).create();
        alertDialog.setMessage("\t\t\t\tAre you ready?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes!",
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
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        Log.d("My Log", "init Game nach Dialog");

*/
        Log.d("My Log", "after start()");
        routingToTableLayout();


    }


            private void initDummyOpp() {
                Battlefield tmpBattlefield = new Battlefield(8);

                DummyOppShip ship1 = new DummyOppShip(5);
                tmpBattlefield.addShip(ship1);
                opponent.setShips(ship1);

                // dont know





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
        //tabHost.setCurrentTab(0);

        Random r = new Random();
        Cordinate c;

        do {
            c = new Cordinate(r.nextInt(8), r.nextInt(8));
        } while (opponent.getBattleFieldByCordinate(c) != 0);

        int[][] tmpMyShips = myPlayer.getShips();

        if (tmpMyShips[c.x][c.y] == -1) {
            opponent.updateBattleField(c, -1);
        } else if (tmpMyShips[c.x][c.y] == 1) {
            playSoundHitShip();
            phoneVibrate();

            opponent.updateBattleField(c, 1);
            if (myPlayer.incShipDestroyed() == myPlayer.getMaxShips()) {
                endGame(opponent);
            }
        }

        displayOpponentsBattleField();
        Context context = getApplicationContext();
        CharSequence text = c.x + "" + c.y;
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, text, duration).show();

        game.newMove(new Move(opponent, myPlayer, c));


        //waiting.run(tabHost,1);
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            public void run() {
                tabHost.setCurrentTab(1);
                toggleWindowTouchable();
            }
        }, 1000);


    }

    private void realOpponentsMove() {
        //tabHost.setCurrentTab(0);

        //Opponent has quit
        if (oppMove.charAt(0) == 'c') {
            Toast.makeText(getBaseContext(), "The enemy gave up!",
                    Toast.LENGTH_LONG).show();
            hitHomeButton = false;
            toStartScreen();
        } else {
            Cordinate c = new Cordinate((int) oppMove.charAt(0) - 48, (int) oppMove.charAt(1) - 48);

            int[][] tmpMyShips = myPlayer.getShips();


            if (tmpMyShips[c.x][c.y] == -1) {
                opponent.updateBattleField(c, -1);
                playMissSound.start();
                Log.d("My Log:", "nicht getroffen" + String.valueOf(tmpMyShips[c.x][c.y]));
            } else if (tmpMyShips[c.x][c.y] == 1) {
                playHitSound.start();
                phoneVibrate();
                opponent.updateBattleField(c, 1);
                if (myPlayer.incShipDestroyed() == myPlayer.getMaxShips()) {
                    endGame(opponent);

                }

                //displayOpponentsBattleField();


                //display Opponents shot
                Context context = getApplicationContext();
                CharSequence text = c.x + "" + c.y;
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(context, text, duration).show();

            }
            game.newMove(new Move(opponent, myPlayer, c));
            displayOpponentsBattleField();
        }
        //waiting.run(tabHost,1);

        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            public void run() {
                tabHost.setCurrentTab(1);
                toggleWindowTouchable();
            }
        }, 1000);

    }

    private void startCounter() {

        if (intent.getBooleanExtra("WIFI", true)) {
            host = getIntent().getBooleanExtra("IsHost", true);
            send = getIntent().getStringExtra("SHIPS");
            //On coordinates of the ships get send on zero
            alertDialog = new AlertDialog.Builder(GameActivity2.this).create();

            if (host) {
                Log.d("My Log", "send" + send);

                alertDialog.setMessage("\t\t\t\tGame is starting");
                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();


                new CountDownTimer(10000, 500) {
                    String wait = ".";

                    @Override
                    public void onTick(long millisUntilFinished) {
                        alertDialog.setMessage("\t\t\t\tGame is starting" + wait);
                        if (wait.length() < 3) {
                            wait = wait + ".";
                        } else {
                            wait = "";
                        }

                    }

                    @Override
                    public void onFinish() {
                        serverThread.dataReady(send);
                    }
                }.start();


            } else if (!host) {

                alertDialog.setMessage("\t\t\t\tGame starts in  ");
                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();

                new CountDownTimer(10000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        alertDialog.setMessage("\t\t\t\tGame starts in  " + (millisUntilFinished / 1000));
                    }

                    @Override
                    public void onFinish() {
                        //info.setVisibility(View.GONE);
                        alertDialog.dismiss();

                        if (!host) {
                            Log.d("My Log", "send" + send);
                            clientThread.dataReady(send);
                        }
                    }
                }.start();
            }
        }
    }

    private void displayMyShips() {
        int[][] ships = myPlayer.getShips();

        TextView tv;
        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {

                tv = (TextView) findViewById(getRoutingByCordinateMyField(new Cordinate(i, j)));

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

        Log.d("My Log:", "nicht getroffen hugo " + opBattleField[0][0]);
        int[][] myShips = myPlayer.getShips();

        TextView tv;
        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {

                tv = (TextView) findViewById(getRoutingByCordinateMyField(new Cordinate(i, j)));

                tv.setTextSize(20);
                tv.setTextColor(Color.WHITE);
                if (opBattleField[i][j] == 1) {
                    tv.setBackgroundResource(R.mipmap.sea_ship_destroyed);
                } else if (opBattleField[i][j] == -1) {
                    tv.setBackgroundResource(R.mipmap.sea_wronghit);
                    Log.d("My Log:", "nicht getroffen hugo");
                } else if (opBattleField[i][j] == -1 && Character.getNumericValue(oppMove.charAt(0)) == i
                        && Character.getNumericValue(oppMove.charAt(1)) == j) {
                    tv.setBackgroundResource(R.mipmap.sea_wronghit);
                    Log.d("My Log:", "nicht getroffen hugo");
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

        int[][] opBattleField = opponent.getBattleField();

        TextView tv;
        for (int i = 0; i < game.getSize(); i++) {
            for (int j = 0; j < game.getSize(); j++) {
                tv = (TextView) findViewById(getRoutingByCordinateMyField(new Cordinate(i, j)));
                if (opBattleField[i][j] == 1) {
                    tv.setBackgroundResource(R.mipmap.sea_ship);
                } else if (opBattleField[i][j] == -1) {
                    tv.setBackgroundResource(R.mipmap.sea_wronghit);
                } else if (opBattleField[i][j] == 0) {
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

                tv = (TextView) findViewById(getRoutingByCordinateOpponentField(new Cordinate(i, j)));

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


        routingOpponentField = new int[8][8];

        routingOpponentField[0][0] = findViewById(R.id.opponentTextView00).getId();
        routingOpponentField[0][1] = findViewById(R.id.opponentTextView01).getId();
        routingOpponentField[0][2] = findViewById(R.id.opponentTextView02).getId();
        routingOpponentField[0][3] = findViewById(R.id.opponentTextView03).getId();
        routingOpponentField[0][4] = findViewById(R.id.opponentTextView04).getId();
        routingOpponentField[0][5] = findViewById(R.id.opponentTextView05).getId();
        routingOpponentField[0][6] = findViewById(R.id.opponentTextView06).getId();
        routingOpponentField[0][7] = findViewById(R.id.opponentTextView07).getId();

        routingOpponentField[1][0] = findViewById(R.id.opponentTextView10).getId();
        routingOpponentField[1][1] = findViewById(R.id.opponentTextView11).getId();
        routingOpponentField[1][2] = findViewById(R.id.opponentTextView12).getId();
        routingOpponentField[1][3] = findViewById(R.id.opponentTextView13).getId();
        routingOpponentField[1][4] = findViewById(R.id.opponentTextView14).getId();
        routingOpponentField[1][5] = findViewById(R.id.opponentTextView15).getId();
        routingOpponentField[1][6] = findViewById(R.id.opponentTextView16).getId();
        routingOpponentField[1][7] = findViewById(R.id.opponentTextView17).getId();

        routingOpponentField[2][0] = findViewById(R.id.opponentTextView20).getId();
        routingOpponentField[2][1] = findViewById(R.id.opponentTextView21).getId();
        routingOpponentField[2][2] = findViewById(R.id.opponentTextView22).getId();
        routingOpponentField[2][3] = findViewById(R.id.opponentTextView23).getId();
        routingOpponentField[2][4] = findViewById(R.id.opponentTextView24).getId();
        routingOpponentField[2][5] = findViewById(R.id.opponentTextView25).getId();
        routingOpponentField[2][6] = findViewById(R.id.opponentTextView26).getId();
        routingOpponentField[2][7] = findViewById(R.id.opponentTextView27).getId();

        routingOpponentField[3][0] = findViewById(R.id.opponentTextView30).getId();
        routingOpponentField[3][1] = findViewById(R.id.opponentTextView31).getId();
        routingOpponentField[3][2] = findViewById(R.id.opponentTextView32).getId();
        routingOpponentField[3][3] = findViewById(R.id.opponentTextView33).getId();
        routingOpponentField[3][4] = findViewById(R.id.opponentTextView34).getId();
        routingOpponentField[3][5] = findViewById(R.id.opponentTextView35).getId();
        routingOpponentField[3][6] = findViewById(R.id.opponentTextView36).getId();
        routingOpponentField[3][7] = findViewById(R.id.opponentTextView37).getId();

        routingOpponentField[4][0] = findViewById(R.id.opponentTextView40).getId();
        routingOpponentField[4][1] = findViewById(R.id.opponentTextView41).getId();
        routingOpponentField[4][2] = findViewById(R.id.opponentTextView42).getId();
        routingOpponentField[4][3] = findViewById(R.id.opponentTextView43).getId();
        routingOpponentField[4][4] = findViewById(R.id.opponentTextView44).getId();
        routingOpponentField[4][5] = findViewById(R.id.opponentTextView45).getId();
        routingOpponentField[4][6] = findViewById(R.id.opponentTextView46).getId();
        routingOpponentField[4][7] = findViewById(R.id.opponentTextView47).getId();

        routingOpponentField[5][0] = findViewById(R.id.opponentTextView50).getId();
        routingOpponentField[5][1] = findViewById(R.id.opponentTextView51).getId();
        routingOpponentField[5][2] = findViewById(R.id.opponentTextView52).getId();
        routingOpponentField[5][3] = findViewById(R.id.opponentTextView53).getId();
        routingOpponentField[5][4] = findViewById(R.id.opponentTextView54).getId();
        routingOpponentField[5][5] = findViewById(R.id.opponentTextView55).getId();
        routingOpponentField[5][6] = findViewById(R.id.opponentTextView56).getId();
        routingOpponentField[5][7] = findViewById(R.id.opponentTextView57).getId();

        routingOpponentField[6][0] = findViewById(R.id.opponentTextView60).getId();
        routingOpponentField[6][1] = findViewById(R.id.opponentTextView61).getId();
        routingOpponentField[6][2] = findViewById(R.id.opponentTextView62).getId();
        routingOpponentField[6][3] = findViewById(R.id.opponentTextView63).getId();
        routingOpponentField[6][4] = findViewById(R.id.opponentTextView64).getId();
        routingOpponentField[6][5] = findViewById(R.id.opponentTextView65).getId();
        routingOpponentField[6][6] = findViewById(R.id.opponentTextView66).getId();
        routingOpponentField[6][7] = findViewById(R.id.opponentTextView67).getId();

        routingOpponentField[7][0] = findViewById(R.id.opponentTextView70).getId();
        routingOpponentField[7][1] = findViewById(R.id.opponentTextView71).getId();
        routingOpponentField[7][2] = findViewById(R.id.opponentTextView72).getId();
        routingOpponentField[7][3] = findViewById(R.id.opponentTextView73).getId();
        routingOpponentField[7][4] = findViewById(R.id.opponentTextView74).getId();
        routingOpponentField[7][5] = findViewById(R.id.opponentTextView75).getId();
        routingOpponentField[7][6] = findViewById(R.id.opponentTextView76).getId();
        routingOpponentField[7][7] = findViewById(R.id.opponentTextView77).getId();

    }

    private int getRoutingByCordinateMyField(Cordinate c) {
        return routingMyField[c.x][c.y];
    }

    private int getRoutingByCordinateOpponentField(Cordinate c) {
        return routingOpponentField[c.x][c.y];
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

    public void randomAttack(int count) {

        if (intent.getBooleanExtra("WIFI", true)) {


            if (myPlayer.getRandomAttacks() > 0) {
                tempRoundCount++;
                Cordinate randomShipCordinate = (new randomShipCordinate(opponent, game)).c;
                Cordinate randomWaterCordinate = (new randomWaterCordinate(opponent)).c;
                Random r = new Random();

                if (r.nextInt(10) >= 4) {                               // increased chance to hit a ship
                    myPlayer.updateBattleField(randomShipCordinate, 1);
                    playHitSound.start();
                    if (opponent.incShipDestroyed() == opponent.getMaxShips()) {
                        endGame(myPlayer);
                    }
                    myPlayer.decRandomAttacks();
                    game.newMove(new Move(myPlayer, opponent, randomShipCordinate));
                    displayMyBattleField();
                    Toast.makeText(getBaseContext(), "Verbleibende Zufallsangriffe: " + myPlayer.getRandomAttacks(),
                            Toast.LENGTH_LONG).show();
                    aiOpponentsMove();

                } else {
                    myPlayer.updateBattleField(randomWaterCordinate, -1);
                    playMissSound.start();
                    if (opponent.incShipDestroyed() == opponent.getMaxShips()) {
                        endGame(myPlayer);
                    }
                    myPlayer.decRandomAttacks();
                    game.newMove(new Move(myPlayer, opponent, randomWaterCordinate));
                    displayMyBattleField();
                    aiOpponentsMove();
                }

            } else {
                Toast.makeText(getBaseContext(), "Bleib doch fair...",
                        Toast.LENGTH_LONG).show();
                displayMyBattleField();

                aiOpponentsMove();

            }
        }
    }

    @Override
    public void onBackPressed() {
        Log.d("my Log", " Game Activity onStop()");
        super.onBackPressed();
        hitHomeButton = false;
        giveUp();
    }

    @Override
    public void onStop() {
        Log.d("my Log", " Game Activity onStop()");
        //if HomeButton gets pressed, cut connection
        super.onStop();
        if (hitHomeButton) {
            giveUp();
        }

    }

    private void giveUp() {
        if (intent.getBooleanExtra("WIFI", false)) {

            //Ends the game on the other Device
            if (host) {
                serverThread.dataReady("c");
            } else {
                clientThread.dataReady("c");
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        //go to homescreen
        toStartScreen();
    }

    private void toggleWindowTouchable() {
        if (touchable) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            touchable = false;
        } else if (!touchable) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            touchable = true;
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






