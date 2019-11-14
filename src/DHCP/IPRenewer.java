package DHCP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ConcurrentHashMap;

public class IPRenewer extends Thread {
    private DatagramSocket socket;
    private ConcurrentHashMap<Integer, IPRenew> renewHashMap;
    private Integer port;
    private InetAddress address;

    IPRenewer(DatagramSocket socket, ConcurrentHashMap<Integer, IPRenew> renewHashMap, Integer port, InetAddress address) {
        this.socket = socket;
        this.renewHashMap = renewHashMap;
        this.port = port;
        this.address = address;
    }

    @Override
    public void run() {
        // add thing to not renew if no renew can be made;
        renewHashMap.get(port).renew();
        String message = "Renewed" + port;
        byte[] buf = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
