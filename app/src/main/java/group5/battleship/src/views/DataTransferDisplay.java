package group5.battleship.src.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import group5.battleship.R;
import group5.battleship.src.wifi.ClientThread;
import group5.battleship.src.wifi.ServerThread;

/**
 * Created by Bernhard on 18.04.17.
 */

public class DataTransferDisplay extends Activity {

    TextView p1TextView;
    TextView p2TextView;

    ClientThread clientThread;
    ServerThread serverThread;

    InetAddress hostAddress;
    String stringHostAddress;

    Timer myTimer;
    TimerTask myTask;

    Intent intent;
    Boolean host;
    static String send = "hihi";

    int port = 8888;

    Button button;                                                                          /////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_transfer_display);

        p1TextView = (TextView) findViewById(R.id.player1TextView);
        p2TextView = (TextView) findViewById(R.id.player2TextView);
        button = (Button) findViewById(R.id.button4);                                       //////////////

        intent = getIntent();

        //Check for the connection
        if(intent.getBooleanExtra("Connected", false)) {
            //Get hostaddress as string
            stringHostAddress = intent.getStringExtra("HostAddress");

            //Convert that to Inet Address
            try{
                hostAddress = InetAddress.getByName(stringHostAddress);

            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        //Determine if this user is host or client
        host = intent.getBooleanExtra("IsHost", false);

        //the Host runs the Server-Thread, Client runs the Client-Thread
        if (host) {
            serverThread = new ServerThread(port);
            new Thread(serverThread).start();
        }
        else {
            clientThread = new ClientThread(hostAddress, port);
            new Thread(clientThread).start();
        }


    }

    //The display runs on a timer and updates the UI as packets are received.
    //The hostdevice will display host for player 1, and upon receiving the packets from client
    //Client will display for player 2
    @Override
    public void onResume(){
        //Receice automaticly

        myTimer = new Timer();
        myTask = new TimerTask() {
            @Override
            public void run() {
                if (host) {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            p1TextView.setText("Player 1: " + serverThread.getPlayer1String());
                            p2TextView.setText("Player 2: " + serverThread.getPlayer2String());
                        }
                    };
                    runOnUiThread(runnable);
                }
                else if (!host) {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            p1TextView.setText("Player 1: " + clientThread.getPlayer1String());
                            p2TextView.setText("Player 2: " + clientThread.getPlayer2String());

                        }
                    };
                    runOnUiThread(runnable);
                }
            }
        };

        myTimer.schedule(myTask, 1, 1);
        super.onResume();
    }


   public void onClick(View view){//This was the recieve on Click funktion
/*
       Toast.makeText(this, "ButtonClick", Toast.LENGTH_LONG).show();///////////////////////Debugging
       if (host) {
           Runnable runnable = new Runnable() {
               @Override
               public void run() {

                   p1TextView.setText("Player 1: " + serverThread.getPlayer1String());
                   p2TextView.setText("Player 2: " + serverThread.getPlayer2String());
               }
           };
           runOnUiThread(runnable);
       }
       else if (!host) {
           Runnable runnable = new Runnable() {
               @Override
               public void run() {

                   p1TextView.setText("Player 1: " + clientThread.getPlayer1String());
                   p2TextView.setText("Player 2: " + clientThread.getPlayer2String());
               }
           };
           runOnUiThread(runnable);
       }*/
   }
/*
   public void onTextClick(View view){
       EditText editText = (EditText)findViewById(R.id.editText2);

       String send = editText.getText().toString();
       if (host){
           serverThread.dataReady(send);
       }
       else if (!host){
           clientThread.dataReady(send);
       }
   }
   */

    public void onTextClick(View view){
        EditText editText = (EditText)findViewById(R.id.editText2);

        send = editText.getText().toString();
        if (host){
            serverThread.dataReady(send);
        }
        else if (!host){
            clientThread.dataReady(send);

        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());

    }


}
