package DHCP;

import inet.ipaddr.AddressStringException;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class DHCPDriver {

    public static void main(String[] args) throws IOException {
        try {
            Duration expiry = Duration.ofSeconds(60);
            IPAddress net = new IPAddressString("192.168.1.0/24").toAddress();
            IPList iplist =
                    StreamSupport.stream(net.getIterable().spliterator(), false)
                            .map(IP::new)
                            .collect(Collectors.toCollection(IPList::new));
            System.out.println(iplist);
            ConcurrentHashMap<Integer, IPRenew> checkedIn = new ConcurrentHashMap<>();
            DHCPRenewer renewer = new DHCPRenewer(checkedIn, iplist, expiry);
            renewer.setDaemon(true);
            renewer.start();
            DatagramSocket socket = new DatagramSocket(7070);
            while (true) {
                byte[] buf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                String message = utils.Utils.responseToString(packet.getData());
                if (message.contains("renew")) {
                    IPRenewer ipRenewer = new IPRenewer(socket, checkedIn, packet.getPort(), packet.getAddress());
                    ipRenewer.start();
                } else if (message.contains("release")) {
                    System.out.println("relsesed");
                } else {
                    var runner = new DHCPRunner(socket, packet, iplist, net, checkedIn);
                    runner.start();
                }
            }


        } catch (AddressStringException e) {
            e.printStackTrace();
        }


    }
}
