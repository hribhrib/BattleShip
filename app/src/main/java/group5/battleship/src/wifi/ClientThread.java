package group5.battleship.src.wifi;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import group5.battleship.src.views.DataTransferDisplay;

/**
 * Created by Bernhard on 18.04.17.
 */

public class ClientThread implements Runnable {

    private InetAddress myHostAddress;
    private int myPort = 0;

    private DatagramSocket socket;
    private byte[] sendData = new byte[64];
    private byte[] receiveData = new byte[64];

    private String player1String = "Client";
    private String player2String;

    private int receiveCount = 0;

    private boolean dataReady = false;
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
            while (true) {
                Log.d("#######################", "CLIENT_Round"+i);
                i++;
                try {
                    //only on first cycle
                    if (socket == null) {
                        socket = new DatagramSocket(myPort);
                        socket.setSoTimeout(60000);
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
                    sendData = (dataToSend + i).getBytes();

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

                //receive
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                Log.e("MyTag", "CLIENT Waiting for Packet");
                try {

                    socket.receive(receivePacket);
                    receivePacket.getData();

                    player2String = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    Log.e("MyTag", "Received Packet, contained: " + player2String);
                    receiveCount++;


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

    /////////////////////////////////////////////////////////////////////////////////////////////////////
/*
    public void sendData() {

        try {
            //only on first cycle
            if (socket == null) {
                socket = new DatagramSocket(myPort);
                //socket.setSoTimeout(1);
            }
        } catch (IOException e) {
            if (e.getMessage() == null) {
                Log.e("Set Socket", "Unkown Message");
            } else {
                Log.e("Set Socket", e.getMessage());
            }
        }
        try {
            //sendData = (player1String + sendCount).getBytes();
            sendData = ("My first con over the net.. " ).getBytes();


            //UDP Packet is created using this data, its length and destination info
            DatagramPacket packet = new DatagramPacket(sendData, sendData.length,
                    myHostAddress, myPort);

            socket.send(packet);
            Log.e("MyTag", "Client: Packet sent");

        } catch (IOException e) {
            if (e.getMessage() == null) {
                Log.e("Set Socket", "Unkonwn Message: Likley Timeout");
            } else {
                Log.e("Set Socket", e.getMessage());
            }
        }

    }

    public void receiveData(){
        try {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            socket.receive(receivePacket);
            receivePacket.getData();

            player2String = new String(receivePacket.getData(), 0, receivePacket.getLength());
            receiveCount++;
            //dataTransferDisplay.interact();                                                     //From me


        } catch (IOException e) {
            if (e.getMessage() == null) {
                Log.e("Set Socket", "Unkown Message");
            } else {
                Log.e("Set Socket", e.getMessage());
            }
        }

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////////
*/
    public String getPlayer1String() {
        return player1String;
    }

    public String getPlayer2String() {
        return (player2String + receiveCount);
    }

    public synchronized void dataReady(String dataToSend) {
        this.dataToSend = dataToSend;
        dataReady = true;
        notifyAll();
    }


}
