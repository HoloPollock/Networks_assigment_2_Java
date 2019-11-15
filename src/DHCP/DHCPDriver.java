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
                            .collect(Collectors.toCollection(IPList::new)); //create list of all possible IPs to assign and Atomicbooleans set to false as they are not assigned
            System.out.println(iplist);
            ConcurrentHashMap<Integer, IPRenew> checkedIn = new ConcurrentHashMap<>(); //create empty shared map to hold renew infor
            DHCPRenewer renewer = new DHCPRenewer(checkedIn, iplist, expiry); //create Class to check if things should be dropped
            renewer.setDaemon(true);
            renewer.start(); //start as daemon
            DatagramSocket socket = new DatagramSocket(7070); //create new socket
            while (true) {
                byte[] buf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length); //receive information
                socket.receive(packet);

                String message = utils.Utils.responseToString(packet.getData());
                if (message.contains("renew")) { //renew IP
                    IPRenewer ipRenewer = new IPRenewer(socket, checkedIn, packet.getPort(), packet.getAddress());
                    ipRenewer.start();
                } else if (message.contains("release")) { //release IP
                    DHCPReleser releser = new DHCPReleser(iplist, checkedIn, socket, packet.getPort(), packet.getAddress());
                    releser.start();
                    System.out.println("released");
                } else { //assign I{
                    var runner = new DHCPRunner(socket, packet, iplist, net, checkedIn);
                    runner.start();
                }
            }


        } catch (AddressStringException e) {
            e.printStackTrace();
        }


    }
}
