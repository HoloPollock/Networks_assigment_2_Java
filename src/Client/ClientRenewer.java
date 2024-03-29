package Client;

import utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

public class ClientRenewer extends Thread {
    private String ip; // while this isn't used it exists and is used by the constructor as this Class should not be used unless string IP can be set
    private DatagramSocket socket; //Socket to send data on;
    private Duration renew; //time of lease

    //Create a new timer task
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            try {
                renew();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public ClientRenewer(String ip, DatagramSocket socket, Duration renew) {
        this.ip = ip;
        this.socket = socket;
        this.renew = renew;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    @Override
    public void run() {
        if (Thread.currentThread().isDaemon()) {
            Timer timer = new Timer("renewer");
            timer.scheduleAtFixedRate(task, renew.dividedBy(3).toMillis(), renew.dividedBy(2).toMillis()); // run every renew time/2
        } else {
            System.out.println("should be run as Daemon");
        }
    }

    private void renew() throws IOException {
        if (ip != null) { //if there is an IP to renew
            byte[] buf = "renew".getBytes(); // create the renew message
            DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getLocalHost(), 7070); //send to DHCP server on port 7070
            socket.send(packet); //Send Datagram
            byte[] bufRec = new byte[512];
            DatagramPacket message = new DatagramPacket(bufRec, bufRec.length);
            socket.receive(message);
            String ack = Utils.responseToString(message.getData()).strip(); // get confirmation it worked
//            System.out.println(ack);
        }
    }
}
