package DNS;

import com.google.gson.Gson;
import models.DNSIP;
import utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;

public class DNSRunner extends Thread {
    private HashMap<String, DNSIP> dnsMap;
    private DatagramSocket socket;
    private DatagramPacket packet;

    public DNSRunner(HashMap<String, DNSIP> dnsMap, DatagramSocket socket, DatagramPacket packet) {
        this.dnsMap = dnsMap;
        this.socket = socket;
        this.packet = packet;
    }

    @Override
    public void run() {
        Gson gson = new Gson();
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        String url = Utils.responseToString(packet.getData()).strip(); // get url requested
        DNSIP ips = dnsMap.get(url); // get mapping from url
        if (ips != null) {
            String message = gson.toJson(ips);
            byte[] buf = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("no url");
            String errorMessage = "url not in DNS server";
            byte[] buf = errorMessage.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
