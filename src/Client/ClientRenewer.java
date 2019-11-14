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
    private String ip; // while this isnt used it exists and is used by the constructor as this Class should not be used unless string IP can be set
    private DatagramSocket socket;
    private Duration renew;

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

    @Override
    public void run() {
        if (Thread.currentThread().isDaemon()) {
            Timer timer = new Timer("renewer");
            timer.scheduleAtFixedRate(task, renew.dividedBy(3).toMillis(), renew.dividedBy(2).toMillis());
        } else {
            System.out.println("should be run as Daemon");
        }
    }

    private void renew() throws IOException {
        byte[] buf = "renew".getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getLocalHost(), 7070);
        socket.send(packet);
        byte[] bufRec = new byte[512];
        DatagramPacket message = new DatagramPacket(buf, buf.length);
        // add a packet lost thing
        socket.receive(message);
        String ack = Utils.responseToString(message.getData()).strip();
        System.out.println(ack);
    }
}
