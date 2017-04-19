package group5.battleship.src.wifi;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by Bernhard on 18.04.17.
 */

public class ClientThread implements Runnable{

    private InetAddress myHostAddress;
    private int myPort = 0;

    private DatagramSocket socket;
    private byte[] sendData = new byte[64];
    private byte[] receiveData = new byte[64];

    private String player1String = "Client";
    private String player2String;
    private int sendCount = 1;
    private int receiveCount = 0;

    //the datatransfer activity passes the adress of the group host and the port
    //for use in the thread
    public ClientThread(InetAddress hostAddress, int port) {
        myHostAddress = hostAddress;
        myPort = port;
    }

    @Override
    public void run() {

        //Confirm that the host address and port are established
        if (myHostAddress != null && myPort != 0) {

            while (true) {
                try {
                    if (socket == null) {
                        socket = new DatagramSocket(myPort);
                        socket.setSoTimeout(1);
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
                    sendData = (player1String + sendCount).getBytes();
                    sendCount++; //keps track how many have been send

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

                //receive
                try {
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    socket.receive(receivePacket);
                    receivePacket.getData();

                    player2String = new String(receivePacket.getData(), 0, receivePacket.getLength());
                    receiveCount++;

                } catch (IOException e) {
                    if (e.getMessage() == null) {
                        Log.e("Set Socket", "Unkown Message");
                    } else {
                        Log.e("Set Socket", e.getMessage());
                    }
                    continue;
                }
            }
        }
    }

    public String getPlayer1String() {
        return player1String;
    }

    public String getPlayer2String() {
        return (player2String + receiveCount);
    }
}
