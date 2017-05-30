package group5.battleship.src.wifi;

import android.util.Log;
import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Bernhard on 18.04.17.
 */

public class ClientThread implements Runnable, Serializable {

    private InetAddress myHostAddress;
    private int myPort = 0;
    private DatagramSocket socket;
    private byte[] sendData = new byte[64];
    private byte[] receiveData = new byte[64];
    private String player2String;
    private boolean dataReady = false;
    private boolean active = true;
    private String dataToSend;



    //the datatransfer activity passes the adress of the group host and the port
    //for use in the thread
    public ClientThread(InetAddress hostAddress, int port) {
        myHostAddress = hostAddress;
        myPort = port;
    }

    @Override
    public synchronized void run() {

        //Confirm that the host address and port are established
        if (myHostAddress != null && myPort != 0) {

            int i = 0;
            while (active) {
                Log.d("#######################", "CLIENT_Round"+i);
                i++;
                try {
                    //only on first cycle
                    if (socket == null) {
                        Log.d("MY LOG", "New ClientSocket");
                        Log.d("MY LOG", myHostAddress+" HostAdress");
                        socket = new DatagramSocket(myPort);
                        //Time socket waits until connection get´ lost.
                        socket.setSoTimeout(1200000);
                    }
                } catch (IOException e) {
                    if (e.getMessage() == null) {
                        Log.e("Set Socket", "Unkown Message");
                    } else {
                        Log.e("Set Socket", e.getMessage());
                    }
                }

                //ready to send
                try {
                    //Wait until Data is ready
                    while (!dataReady) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    dataReady = false;
                    sendData = (dataToSend).getBytes();

                    //UDP Packet is created using this data, its length and destination info
                    DatagramPacket packet = new DatagramPacket(sendData, sendData.length,
                            myHostAddress, myPort);

                    socket.send(packet);
                    Log.e("MyTag", "Client: Packet sent" );

                } catch (IOException e) {
                    if (e.getMessage() == null) {
                        Log.e("Set Socket", "Unkonwn Message: Likley Timeout");
                        continue;
                    } else {
                        Log.e("Set Socket", e.getMessage());
                    }
                }

                //receive Data
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                Log.e("MyTag", "CLIENT Waiting for Packet");
                try {

                    socket.receive(receivePacket);
                    receivePacket.getData();

                    player2String = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    Log.e("MyTag", "Received Packet, contained: " + player2String);

                } catch (IOException e) {
                    if (e.getMessage() == null) {
                        Log.e("Set Socket", "Unkown Message dep");
                    } else {
                        Log.e("Set Socket dep", e.getMessage());
                    }
                    continue;
                }
            }
        }
    }

    public String getPlayer2String() {
        return (player2String);
    }

    public synchronized void dataReady(String dataToSend) {
        //Tells the client when data is ready, so he continue to work
        this.dataToSend = dataToSend;
        dataReady = true;
        notifyAll();
    }

    public void close() {
        active = false;
        socket.close();
    }


}
