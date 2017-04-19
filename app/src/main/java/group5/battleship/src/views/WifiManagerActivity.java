package group5.battleship.src.views;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;
import group5.battleship.R;
import group5.battleship.src.wifi.WifiBroadcastReciever;


/**
 * Created by Bernhard on 18.04.17.
 */

public class WifiManagerActivity extends AppCompatActivity {


    WifiP2pManager.Channel myChannel;
    WifiP2pManager myManager;
    public TextView myTextView;
    ListView myListView;
    ArrayAdapter<String> wifiP2PAdapter;
    Button searchButton;
    Button playButton;
    WifiBroadcastReciever myReceiver;
    IntentFilter intentFilter;
    Intent dataDisplay;
    // final Intent playIntent = new Intent(this, SetShipsActivity.class);

    Context context;///////////////////////////////////////////////////////////////////////Debugging


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = getApplicationContext();/////////////////////////////////////////////////Debugging

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


        myTextView = (TextView) findViewById(R.id.textView2);
        myListView = (ListView) findViewById(R.id.listView);

        wifiP2PAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        myListView.setAdapter(wifiP2PAdapter);

        searchButton = (Button) findViewById(R.id.button2);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(v);
            }
        });
        playButton = (Button) findViewById(R.id.button3);
        playButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(context, "Click", Toast.LENGTH_LONG).show();///////////////////////Debugging

            }
        }));

        myManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        myChannel = myManager.initialize(this, getMainLooper(), null);
        myReceiver = new WifiBroadcastReciever(myManager, myChannel, this);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //position = myListView.getSelectedItemPosition();

                //Toast.makeText(context, "Position"+(position), Toast.LENGTH_LONG).show();///////////////////////Debugging

                myReceiver.connect(position);
            }
        });

    }

    public void search(View v) {
        myManager.discoverPeers(myChannel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {
                myTextView.setText("Wifi-Direct: Searching...");
            }

            @Override
            public void onFailure(int reason) {
                myTextView.setText("Error: Code " + reason);

            }
        });
    }


    //is called from BroadcastReceiver, once a connection has been made
    // and status as host or client has determined
    public void play(InetAddress hostAddress, Boolean host) {

        dataDisplay.putExtra("HostAddress", hostAddress.getHostAddress()); //Address of the host
        dataDisplay.putExtra("IsHost", host);   //Is this device the host
        dataDisplay.putExtra("Connected", true); //Was connection succesul

        startActivity(dataDisplay);


    }

    public void displayPeers(WifiP2pDeviceList peerList) {

        wifiP2PAdapter.clear();

        for (WifiP2pDevice peer : peerList.getDeviceList()) {
            wifiP2PAdapter.add(peer.deviceName + "\n" + peer.deviceAddress);
            //Toast.makeText(context, "Peer", Toast.LENGTH_SHORT).show();///////////////////////Debugging
        }
    }


    //register the brodcast receiver with the intent values to be matched
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, intentFilter);
    }

    //unregister the brodcast receiver
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);

    }


}
