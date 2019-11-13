package Client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

public class ClientRenewer extends Thread {
    String ip;
    DatagramSocket socket;
    Duration renew;

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            try {
                renew();
            } catch (UnknownHostException e) {
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
    public void run(){
        if (Thread.currentThread().isDaemon()) {
            Timer timer = new Timer("renewer");
            timer.scheduleAtFixedRate(task, 0, renew.dividedBy(2).toMillis());
        }

    }

    private void renew() throws UnknownHostException {
        byte[] buf = "renew".getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getLocalHost(), 7070);
    }


}
