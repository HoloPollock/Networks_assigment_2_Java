package DHCP;

import inet.ipaddr.IPAddress;
import inet.ipaddr.ipv4.IPv4Address;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ConcurrentHashMap;

public class DHCPReleser extends Thread {
    IPList ips;
    ConcurrentHashMap<Integer, IPRenew> renewList;
    DatagramSocket socket;
    private Integer port;
    private InetAddress address;

    public DHCPReleser(IPList ips, ConcurrentHashMap<Integer, IPRenew> renewList, DatagramSocket socket, Integer port, InetAddress address) {
        this.ips = ips;
        this.renewList = renewList;
        this.socket = socket;
        this.port = port;
        this.address = address;
    }

    @Override
    public void run() {
        IPAddress ipToDrop = renewList.get(port).address;
        ips.dropUse((IPv4Address) ipToDrop);
        renewList.remove(port);
        System.out.println(ipToDrop);
        System.out.println(ips);
        byte[] buf = "IP Released".getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //send thing over socket
    }

}
