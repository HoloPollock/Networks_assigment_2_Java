package Client;

import com.google.gson.Gson;
import models.ClientPacket;
import models.DHCPConfig;
import models.DNSIP;
import utils.Utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Scanner;

public class ClientRunner extends Thread {
    String name;
    String url;

    public ClientRunner(String url) {
        this.url = url;
    }

    public ClientRunner() {
    }

    @Override
    public void run() {
        try {
            Gson gson = new Gson();
            DatagramSocket socketDHCP = new DatagramSocket();
            socketDHCP.connect(InetAddress.getLocalHost(), 7070);
            String getIp = "hello";
            byte[] buf = getIp.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(buf, buf.length);
            socketDHCP.send(sendPacket);
            buf = new byte[1024];
            DatagramPacket recPacket = new DatagramPacket(buf, buf.length);
            socketDHCP.receive(recPacket);
            String ip = Utils.responseToString(recPacket.getData());
            DHCPConfig config = gson.fromJson(ip, DHCPConfig.class);
            System.out.println(config);
            name = config.gateway;
            ClientRenewer renewer = new ClientRenewer(name, socketDHCP, config.lease);
            renewer.setDaemon(true);
            renewer.start();
            DatagramSocket socketDNS = new DatagramSocket();
            socketDNS.connect(InetAddress.getLocalHost(), config.dnsPort);
            if (url == null) {
                Scanner scanner = new Scanner(System.in);
                System.out.println(Thread.currentThread().getId() + " Enter Url To Get > ");
                String input = scanner.next();
                System.out.println(input);
                url = input.strip();
            }
            buf = url.getBytes();
            sendPacket = new DatagramPacket(buf, buf.length);
            socketDNS.send(sendPacket);
            buf = new byte[1024];
            recPacket = new DatagramPacket(buf, buf.length);
            socketDNS.receive(recPacket);
            String ipsOfUrl = Utils.responseToString(recPacket.getData());
            DNSIP ips = gson.fromJson(ipsOfUrl, DNSIP.class);
            ServerSocket socket = new ServerSocket(0);
            ClientPacket packet = new ClientPacket(ips.getIPv4(), config.gateway, socket.getLocalPort(), url);
            System.out.println(packet);

            while(true){}

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
