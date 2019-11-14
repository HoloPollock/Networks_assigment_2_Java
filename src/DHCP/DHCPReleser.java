package DHCP;

import inet.ipaddr.IPAddress;
import inet.ipaddr.ipv4.IPv4Address;

import java.net.DatagramSocket;
import java.util.concurrent.ConcurrentHashMap;

public class DHCPReleser extends Thread {
    IPList ips;
    ConcurrentHashMap<Integer, IPRenew> renewList;
    DatagramSocket socket;
    Integer port;

    @Override
    public void run() {
        IPAddress ipToDrop = renewList.get(port).address;
        ips.dropUse((IPv4Address) ipToDrop);
        renewList.remove(port);
        System.out.println(ipToDrop);
        System.out.println(ips);
    }

}
