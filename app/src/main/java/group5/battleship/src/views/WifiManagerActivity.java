package group5.battleship.src.views;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import java.net.InetAddress;
import java.util.Locale;

import group5.battleship.R;
import group5.battleship.src.wifi.WifiBroadcastReciever;


/**
 * Created by Bernhard on 18.04.17.
 */

public class WifiManagerActivity extends AppCompatActivity {


    public static WifiP2pManager.Channel myChannel;
    public static WifiP2pManager myManager;
    public TextView myTextView;
    ListView myListView;
    ArrayAdapter<String> wifiP2PAdapter;
    Button searchButton;
    static WifiBroadcastReciever myReceiver;
    static IntentFilter intentFilter;
    Intent setShipIntent;
    boolean standartMod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("my Log", "Wifi Manager onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        intentFilter = new IntentFilter();

        //  Indicates a change in the Wi-Fi P2P status.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        // Indicates a change in the list of available peers.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        // Indicates the state of Wi-Fi P2P connectivity has changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        // Indicates this device's details have changed.
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

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


        Button searchBtn = (Button) findViewById(R.id.searchBtn);
        searchBtn.setText(R.string.searchBtn);


    }


    public void search(View v) {
        myManager.discoverPeers(myChannel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                myTextView.setText(R.string.connection);
            }

            @Override
            public void onFailure(int reason) {
                if (reason == 0) {
                    myTextView.setText(R.string.failureOne);
                } else if (reason == 2) {
                    myTextView.setText(R.string.failureTwo);
                }
            }
        });
    }


    //is called from BroadcastReceiver, once a connection has been made
    // and status as host or client has determined
    public void play(InetAddress hostAddress, Boolean host) {

        Log.d("My Log", "play()");
        setShipIntent.putExtra("HostAddress", hostAddress.getHostAddress()); //Address of the host
        setShipIntent.putExtra("IsHost", host);   //Is this device the host
        setShipIntent.putExtra("Connected", true); //Was connection succesul
        setShipIntent.putExtra("NAME", getIntent().getStringExtra("NAME"));
        setShipIntent.putExtra("WIFI", true);
        setShipIntent.putExtra("Standart Mod",getIntent().getBooleanExtra("Standart Mod",false));
        startActivity(setShipIntent);
    }

    public void displayPeers(WifiP2pDeviceList peerList) {

        wifiP2PAdapter.clear();

        for (WifiP2pDevice peer : peerList.getDeviceList()) {
            wifiP2PAdapter.add(peer.deviceName + "\n" + peer.deviceAddress);
        }
    }


    //register the brodcast receiver with the intent values to be matched
    @Override
    protected void onResume() {
        super.onResume();

        Log.d("my Log", "Wifi Manager onResume");


        myChannel = null;
        myManager = null;
        myTextView = null;
        myListView = null;
        wifiP2PAdapter = null;
        myReceiver = null;
        searchButton = null;
        setShipIntent = null;


        myTextView = (TextView) findViewById(R.id.textView2);
        myListView = (ListView) findViewById(R.id.listView);

        searchButton = (Button) findViewById(R.id.searchBtn);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(v);
            }
        });

        wifiP2PAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        myListView.setAdapter(wifiP2PAdapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myReceiver.connect(position);
            }
        });


        myManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        myChannel = myManager.initialize(this, getMainLooper(), null);
        myReceiver = new WifiBroadcastReciever(myManager, myChannel, this);
        standartMod = getIntent().getBooleanExtra("Standart Mod",false);
        //standart Mod true starts a 8x8 game
        if (standartMod) {
            setShipIntent = new Intent(WifiManagerActivity.this, SetShipsActivity2.class);
        } else {
            setShipIntent = new Intent(WifiManagerActivity.this, SetShipsActivity.class);
        }

        registerReceiver(myReceiver, intentFilter);
    }

    //unregister the brodcast receiver
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);

    }

    public void disconnect() {

        Log.d("MyTag", "Receiver disconnect");
        myReceiver.disconnect();

    }


}
