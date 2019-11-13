package DNS;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;

import utils.Utils;
import Models.DNSPacket;
import com.google.gson.Gson;
import inet.ipaddr.ipv4.IPv4Address;

public class DNSRunner extends Thread {
    HashMap<String, DNSIP> dnsMap;
    DatagramSocket socket;
    DatagramPacket packet;

    public DNSRunner(HashMap<String, DNSIP> dnsMap, DatagramSocket socket, DatagramPacket packet )
    {
        this.dnsMap = dnsMap;
        this.socket = socket;
        this.packet = packet;
    }


    private static void println(Object o) {
        System.out.println(o.toString());
    }

    private static void print(Object o) {
        System.out.print(o.toString());
    }

        @Override
    public void run() {
        Gson gson = new Gson();
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        String url = Utils.responseToString(packet.getData()).strip();
        print(url);
        DNSIP ips = dnsMap.get(url);
        if (ips != null) {
            String message = gson.toJson(ips.IPv4);
            byte[] buf = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
            try {
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(300);
            }
        }
        else {
            println("no url");
        }

    }
}
