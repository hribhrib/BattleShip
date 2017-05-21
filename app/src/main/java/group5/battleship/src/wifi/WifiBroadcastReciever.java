package group5.battleship.src.wifi;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.widget.Toast;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import group5.battleship.src.views.WifiManagerActivity;


/**
 * Created by Bernhard on 04.04.17.
 */

public class WifiBroadcastReciever extends BroadcastReceiver {

    private static WifiP2pManager myManager;
    private static WifiP2pManager.Channel myChannel;
    private WifiManagerActivity myManagerActivity;
    private List<WifiP2pDevice> myPeers;
    private List<WifiP2pConfig> myConfigs;
    private WifiP2pDevice myDevice;


    public WifiBroadcastReciever(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                 WifiManagerActivity activity) {
        super();
        this.myManager = manager;
        this.myChannel = channel;
        this.myManagerActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();


        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

            // Determine if Wifi P2P mode is enabled or not, alert
            // the Activity.
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                myManagerActivity.myTextView.setText("Wifi-Direct: Enabled");
            } else {
                myManagerActivity.myTextView.setText("Wifi-Direct: Disabled");
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            // The peer list has changed!  We should probably do something about
            // that.
            myPeers = new ArrayList<>();
            myConfigs = new ArrayList<>();

            if (myManager != null) {
                WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {

                    @Override
                    public void onPeersAvailable(WifiP2pDeviceList peerList) {
                        myPeers.clear();
                        myPeers.addAll(peerList.getDeviceList());

                        myManagerActivity.displayPeers(peerList);

                        for (int i = 0; i < peerList.getDeviceList().size(); i++) {
                            WifiP2pConfig config = new WifiP2pConfig();
                            config.deviceAddress = myPeers.get(i).deviceAddress;
                            myConfigs.add(config);
                        }

                    }
                };
                myManager.requestPeers(myChannel, peerListListener);
            }

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

           if (myManager == null) {
                return;
            }

            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {

                // we are connected with the other device, request connection
                // info to find group owner IP
                Log.d("MyTag", "Connected to p2p network. Requesting network details");
                myManager.requestConnectionInfo(myChannel, infoListener);
            } else {
                // It's a disconnect
            }


            // Connection state changed!  We should probably do something about
            // that.

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            WifiP2pDevice device = (WifiP2pDevice) intent
                    .getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
            Log.d("MyLog", "Device status -" + device.status);
        }
    }


    public void connect(int position) {

        //uses postion to obtain the name and the address od the device to connect to
        WifiP2pConfig config = myConfigs.get(position);
        myDevice = myPeers.get(position);

        //connects the two and shows message that it worked
        myManager.connect(myChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(myManagerActivity, "Connected!", Toast.LENGTH_LONG).show();
                Log.d("MyTag", "Connect Succeeded");

            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(myManagerActivity, "Connection Fail: " + reason, Toast.LENGTH_LONG).show();
                Log.d("MyTag", "Connection Failed: Error " + reason);

            }

        });

    }


    //is user host or not, start play funtion in Manager
    public WifiP2pManager.ConnectionInfoListener infoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            InetAddress groupOwnerAddress = info.groupOwnerAddress;
            if (info.groupFormed) {
                if (info.isGroupOwner) {
                    Toast.makeText(myManagerActivity, "Host", Toast.LENGTH_LONG).show();////////////////////////Debugging
                    myManagerActivity.play(groupOwnerAddress, true);

                } else {
                    Toast.makeText(myManagerActivity, "Client", Toast.LENGTH_LONG).show();////////////////////////Debugging
                    myManagerActivity.play(groupOwnerAddress, false);


                }
            }
        }
    };

    public void disconnect() {

        Log.d("MyTag", "Receiver disconnect");

        myManager.removeGroup(myChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d("MyTag", "Removed Group");
            }

            @Override
            public void onFailure(int reason) {
                Log.d("MyTag", "Remove Group Failed: Error " + reason);
            }
        });

    }
}
