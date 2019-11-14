package Client;

import com.google.gson.Gson;
import models.ClientPacket;
import models.DHCPConfig;
import models.DNSIP;
import utils.Utils;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class ClientRunner extends Thread {
    String name;
    String url;
    ClientRenewer renewer;

    public ClientRunner(String url) {
        this.url = url;
    }

    public ClientRunner() {
    }

    @Override
    public void run() {
        try {
            DatagramSocket socketDHCP = new DatagramSocket();
            DatagramSocket socketDNS = new DatagramSocket();
            DHCPConfig config = DHCP(socketDHCP);
            name = config.gateway;
            this.renewer = new ClientRenewer(name, socketDHCP, config.lease);
            this.renewer.setDaemon(true);
            this.renewer.start();
            DNSIP ips = DNS(config.dnsPort, socketDNS);
            ServerSocket socket = new ServerSocket(0);
            ClientPacket packet = new ClientPacket(ips.getIPv4(), config.gateway, socket.getLocalPort(), url);
            System.out.println(packet);
            while (true) {
                System.out.println(Thread.currentThread().getId() + " What do you want to do");
                System.out.print("> ");
                Scanner scanner = new Scanner(System.in);
                String input = scanner.next();
                System.out.println(input);
                if(input.contains("renew")) {
                    renew(socketDHCP);
                }
                else if(input.contains("release")) {
                    release(socketDHCP);
                }
                else if(input.contains("get_ip")) {
                    getIP(socketDHCP);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DHCPConfig DHCP(DatagramSocket socketDHCP) throws IOException {
        Gson gson = new Gson();
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
        return config;
    }

    private DNSIP DNS(int port, DatagramSocket socketDNS) throws IOException {
        Gson gson = new Gson();
        socketDNS.connect(InetAddress.getLocalHost(), port);
        if (url == null) {
            Scanner scanner = new Scanner(System.in);
            System.out.println(Thread.currentThread().getId() + " Enter Url To Get > ");
            String input = scanner.next();
            System.out.println(input);
            url = input.strip();
        }
        byte[] buf = url.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(buf, buf.length);
        socketDNS.send(sendPacket);
        buf = new byte[1024];
        DatagramPacket recPacket = new DatagramPacket(buf, buf.length);
        socketDNS.receive(recPacket);
        String ipsOfUrl = Utils.responseToString(recPacket.getData());
        DNSIP ips = gson.fromJson(ipsOfUrl, DNSIP.class);
        return ips;
    }

    private void release(DatagramSocket socketDHCP) throws IOException {
        if (name != null) {
            byte[] buf = "release".getBytes();
            DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, InetAddress.getLocalHost(), 7070);
            socketDHCP.send(sendPacket);
            buf = new byte[1024];
            DatagramPacket recPacket = new DatagramPacket(buf, buf.length);
            socketDHCP.receive(recPacket);
            name = null;
            renewer.setIp(null);
            String ack = Utils.responseToString(recPacket.getData()).strip();
            System.out.println(ack);
        }
        else {
            System.out.println("Can't release no IP exists");
            System.out.println("Call get_ip to get new ip");
        }
    }

    private void renew(DatagramSocket socketDHCP) throws IOException {
        if (name != null) {
            byte[] buf = "renew".getBytes();
            DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, InetAddress.getLocalHost(), 7070);
            socketDHCP.send(sendPacket);
            byte[] bufRec = new byte[512];
            DatagramPacket recPacket = new DatagramPacket(bufRec, bufRec.length);
            socketDHCP.receive(recPacket);
            String ack = Utils.responseToString(recPacket.getData()).strip();
            System.out.println(ack);
        }
        else {
            System.out.println("Can't renew no IP exists");
            System.out.println("Getting New IP");
            DHCPConfig config = DHCP(socketDHCP);
            name = config.gateway;
            renewer.setIp(name);
        }

    }

    private void getIP(DatagramSocket socketDHCP) throws IOException {
        if(name == null) {
            DHCPConfig config = DHCP(socketDHCP);
            name = config.gateway;
            renewer.setIp(name);
            System.out.println("New Ip Assigned");
        }
        else {
            System.out.println("already have IP can't get new one");
        }
    }
}
