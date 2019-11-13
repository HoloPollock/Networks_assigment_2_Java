package DNS;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;

public class DNSDriver {
    public static void main(String[] args) throws IOException {
        HashMap<String, DNSIP> mappings = new HashMap<>();
        mappings.put("www.sdxcentral.com", new DNSIP("104.20.242.119", "2606:4700:10::6814:f277"));
        mappings.put("www.lightreading.com", new DNSIP("104.25.195.108", "2606:4700:20::6819:c46c"));
        mappings.put("www.linuxfoundation.org", new DNSIP("23.185.0.2", "2620:12a:8000::2"));
        mappings.put("www.cncf.io", new DNSIP("23.185.0.3", "2620:12a:8000::3"));
        mappings.forEach((key, value) -> {
            System.out.print("{" + key + " : " + value + "}");
            System.out.println();
        });
        DatagramSocket socket = new DatagramSocket(9090);
        while (true) {
            byte[] buf = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);
            System.out.println("damm");
            var runner = new DNSRunner(mappings, socket, packet);
            runner.start();
        }
    }
}
