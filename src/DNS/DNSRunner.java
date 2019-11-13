package DNS;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;

public class DNSRunner extends Thread {
    HashMap<String, DNSIP> dnsMap;
    DatagramSocket socket;
    DatagramPacket packet;
}
