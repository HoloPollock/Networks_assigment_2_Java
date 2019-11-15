package DHCP;

import inet.ipaddr.IPAddress;
import inet.ipaddr.ipv4.IPv4Address;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class to release Ip back to be used
 */
public class DHCPReleser extends Thread {
    IPList ips; // list of possible IP
    ConcurrentHashMap<Integer, IPRenew> renewList; //list holding IP and there lease time with what port is using them
    DatagramSocket socket; //socket to send info
    private Integer port; //what port asked to released
    private InetAddress address; //what address asked to be released

    public DHCPReleser(IPList ips, ConcurrentHashMap<Integer, IPRenew> renewList, DatagramSocket socket, Integer port, InetAddress address) {
        this.ips = ips;
        this.renewList = renewList;
        this.socket = socket;
        this.port = port;
        this.address = address;
    }

    @Override
    public void run() {
        IPAddress ipToDrop = renewList.get(port).address; //get Ip to drop
        ips.dropUse((IPv4Address) ipToDrop); //dro Ip from in use
        renewList.remove(port); //remove from list being checked for lease
        System.out.println(ipToDrop);
        System.out.println(ips);
        byte[] buf = "IP Released".getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        try {
            socket.send(packet); //send ack
        } catch (IOException e) {
            e.printStackTrace();
        }
        //send thing over socket
    }

}
