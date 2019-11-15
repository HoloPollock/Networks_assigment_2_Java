package DHCP;

import com.google.gson.Gson;
import inet.ipaddr.IPAddress;
import inet.ipaddr.ipv4.IPv4Address;
import models.DHCPConfig;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.NoSuchObjectException;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DHCPRunner extends Thread {
    private DatagramSocket socket;
    private DatagramPacket packet;
    private IPList list;
    private IPAddress net;
    private ConcurrentHashMap<Integer, IPRenew> renewHashMap;

    DHCPRunner(DatagramSocket socket, DatagramPacket packet, IPList list, IPAddress net, ConcurrentHashMap<Integer, IPRenew> renewHashMap) {
        this.socket = socket;
        this.packet = packet;
        this.list = list;
        this.net = net;
        this.renewHashMap = renewHashMap;

    }

    private static void println(Object o) {
        System.out.println(o.toString());
    }

    @Override
    public void run() {
        Gson gson = new Gson();
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        println("Handling DHCP Conn From: " + address);
        Optional<IPv4Address> ipOptional = list.getFirstUnused(); //Gets Ip t use
        try {
            if (ipOptional.isPresent()) {
                IPv4Address ip = ipOptional.get(); //unwraps Optional
                IPRenew renew = new IPRenew(ip, Instant.now()); //create a new object tl be added to list to check for expiry
                renewHashMap.put(packet.getPort(), renew);
                DHCPConfig dhcpConfig = new DHCPConfig(net, ip);
                String json = gson.toJson(dhcpConfig);
                byte[] buf = json.getBytes();
                packet = new DatagramPacket(buf, buf.length, address, port); //send info
                socket.send(packet);
            } else {
                println("error");
            }
        } catch (NoSuchObjectException e) {
            String error = "No available DHCP.IP";
            byte[] buf = error.getBytes();
            packet = new DatagramPacket(buf, buf.length, address, port);
            try {
                socket.send(packet);
            } catch (IOException ex) {
                System.exit(300);
            }
        } catch (IOException e) {
            System.exit(300);
        }


    }
}
