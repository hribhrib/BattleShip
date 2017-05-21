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

public class ServerThread implements Runnable, Serializable {

    private int myPort;
    private DatagramSocket socket;
    private InetAddress myClientsAddress;
    private byte[] sendData = new byte[64];
    private byte[] receiveData = new byte[64];
    private String player2String;
    private String dataToSend;
    private boolean dataReady;
    private boolean active = true;


    //the datatransfer activity passes the adress of the group host and the port
    //for use in the thread
    public ServerThread(int intitPort) {
        myPort = intitPort;
    }

    @Override
    public synchronized void run() {


        int i = 0;
        while (active) {
            Log.d("#######################", "SERVER_Round" + i);
            //Open the socket, if its not already done
            try {
                if (socket == null) {
                    Log.d("MY LOG", "New ServerSocket");
                    socket = new DatagramSocket(myPort);
                    socket.setSoTimeout(1200000);
                }
            } catch (IOException e) {
                if (e.getMessage() == null) {
                    Log.e("Set Socket", "Unkown Message");
                } else {
                    Log.e("Set Socket", e.getMessage());
                }
            }


            //receive data from client
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            Log.e("MyTag", "Waiting for Packet");
            try {

                socket.receive(receivePacket);
                receivePacket.getData();

                //Get Shiplist on first cycle
                player2String = new String(receivePacket.getData(), 0, receivePacket.getLength());
                Log.e("MyTag", "Received Packet, contained: " + player2String);

                //get Clients address if its not known, only first time
                if (myClientsAddress == null) {
                    myClientsAddress = receivePacket.getAddress();
                }

            } catch (IOException e) {
                if (e.getMessage() == null) {
                    Log.e("Receive", "Null Exception: Likley Timeout");
                    continue;
                } else {
                    Log.e("Set Socket", e.getMessage());
                    continue;
                }

            }

            //ready to send
            try {

                if (myClientsAddress != null) {
                    while (!dataReady) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    dataReady = false;
                    sendData = (dataToSend).getBytes();
                }

                //UDP Packet is created using this data, its length and destination info
                DatagramPacket packet = new DatagramPacket(sendData, sendData.length,
                        myClientsAddress, myPort);

                socket.send(packet);
                Log.e("MyTag", "Server: Packet sent " + dataToSend);

            } catch (IOException e) {
                if (e.getMessage() == null) {
                    Log.e("Sender", "Null Exception: Likley Timeout");
                    continue;
                } else {
                    Log.e("Sender", e.getMessage());
                }
            }
            i++;
        }
    }



    public String getPlayer2String() {
        return player2String;
    }

    public synchronized void dataReady(String dataToSend) {
        //Tells the server when data is ready, so he continue to work
        this.dataToSend = dataToSend;
        dataReady = true;
        notifyAll();
    }

    public void close() {
        active = false;
        socket.close();
    }
}

