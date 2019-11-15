package Client;

import com.google.gson.Gson;
import models.ClientPacket;
import models.DHCPConfig;
import models.DNSIP;
import utils.Utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientRunner extends Thread {
    private String name;
    private String url;
    private ClientRenewer renewer;
    private boolean repl;

    public ClientRunner(String url, boolean repl) {
        this.url = url;
        this.repl = repl;
    }

    public ClientRunner() {
        repl = false;
    }

    public ClientRunner(String url) {
        this.url = url;
        repl = false;
    }

    public ClientRunner(boolean repl) {
        this.repl = repl;
    }

    @Override
    public void run() {
        try {
            DatagramSocket socketDHCP = new DatagramSocket(); //create socket for DHCP
            DatagramSocket socketDNS = new DatagramSocket(); //create socket for DNS
            DHCPConfig config = DHCP(socketDHCP); //Get Ip
            name = config.gateway;
            this.renewer = new ClientRenewer(name, socketDHCP, config.lease); //Start the automatic rewer
            this.renewer.setDaemon(true);
            this.renewer.start();
            if(url != null && !repl) { //if url is set and not going to repl only query dns and build packet oncce
                System.out.println(url);
                DNSIP ips = DNS(config.dnsPort, socketDNS, url); //Query DNS
                ServerSocket socket = new ServerSocket(0); // get "open" port
                ClientPacket packet = new ClientPacket(ips.getIPv4(), config.gateway, socket.getLocalPort(), url); //build packet
                System.out.println(packet);
            } else if (!repl){ // if url isnt set but noting going to repl
                Scanner scanner = new Scanner(System.in);
                System.out.println(Thread.currentThread().getId() + " Enter Url To Get > ");
                String input = scanner.next(); // get url someone wish to query
                System.out.println(input);
                url = input.strip();
                System.out.println(url);
                try {
                    DNSIP ips = DNS(config.dnsPort, socketDNS, url); // make DNS request
                    ServerSocket socket = new ServerSocket(0);
                    ClientPacket packet = new ClientPacket(ips.getIPv4(), config.gateway, socket.getLocalPort(), url); //make packet
                    System.out.println(packet);
                } catch (NoSuchElementException e){
                    System.out.println("bad url");
                }
            }
            while (repl) { //REPl
                System.out.println(Thread.currentThread().getId() + " What do you want to do");
                System.out.print("> ");
                Scanner scanner = new Scanner(System.in);
                String input = scanner.next();
                if (input.contains("renew")) { //
                    renew(socketDHCP);
                } else if (input.contains("release")) {
                    release(socketDHCP);
                } else if (input.contains("get_ip")) {
                    getIP(socketDHCP);
                } else {
                    try {
                        DNSIP ips = DNS(config.dnsPort, socketDNS, input);
                        ServerSocket socket = new ServerSocket(0);
                        ClientPacket packet = new ClientPacket(ips.getIPv4(), config.gateway, socket.getLocalPort(), input);
                        System.out.println(packet);
                    } catch (NoSuchElementException e) {
                        System.out.println("bad url");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param socketDHCP Socket to connected to DHCP server
     * @return DHCPConfig Class with information from DHCP server including IP assigned
     * @throws IOException
     * This Gets IP form the client
     */
    private DHCPConfig DHCP(DatagramSocket socketDHCP) throws IOException {
        Gson gson = new Gson();
        socketDHCP.connect(InetAddress.getLocalHost(), 7070); //connect to DHCP server
        String getIp = "hello";
        byte[] buf = getIp.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(buf, buf.length); //Send hello message
        socketDHCP.send(sendPacket);
        buf = new byte[1024];
        DatagramPacket recPacket = new DatagramPacket(buf, buf.length); //Receive information
        socketDHCP.receive(recPacket);
        String ip = Utils.responseToString(recPacket.getData());
        DHCPConfig config = gson.fromJson(ip, DHCPConfig.class); //Get Config information from IP
        System.out.println(config);
        return config;
    }

    /**
     * @param port Port DNS server is on
     * @param socketDNS socket for connecting to DNS
     * @param url url to query DNS server with
     * @return DNSIP class holding IPv4 and IPv6 of given Url
     * @throws IOException
     */
    private DNSIP DNS(int port, DatagramSocket socketDNS, String url) throws IOException, NoSuchElementException {
        Gson gson = new Gson();
        socketDNS.connect(InetAddress.getLocalHost(), port);//connect to DNS Server
        byte[] buf = url.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(buf, buf.length); // create packet to be sent with url infomation as bytes
        socketDNS.send(sendPacket);
        buf = new byte[1024];
        DatagramPacket recPacket = new DatagramPacket(buf, buf.length);
        socketDNS.receive(recPacket);
        String ipsOfUrl = Utils.responseToString(recPacket.getData());
        if(ipsOfUrl.contains("url not in DNS server")) {
            throw new NoSuchElementException();
        }
        else {
            DNSIP ips = gson.fromJson(ipsOfUrl, DNSIP.class); //get response from json
            return ips;
        }
    }

    /**
     * @param socketDHCP Socket to connect to DHCP
     * @throws IOException
     */
    private void release(DatagramSocket socketDHCP) throws IOException {
        if (name != null) { //only run if there is a IP to release
            byte[] buf = "release".getBytes();
            DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, InetAddress.getLocalHost(), 7070); // create new packet to send to DHCP
            socketDHCP.send(sendPacket); //send release information
            buf = new byte[1024];
            DatagramPacket recPacket = new DatagramPacket(buf, buf.length);
            socketDHCP.receive(recPacket); //get confirmation
            name = null; //remove IP
            renewer.setIp(null); //Remove IP from auto renewer telling it to stop trying to renew
            String ack = Utils.responseToString(recPacket.getData()).strip();
            System.out.println(ack);
        } else {
            System.out.println("Can't release no IP exists");
            System.out.println("Call get_ip to get new ip");
        }
    }

    /**
     * @param socketDHCP Socket to connect to DHCP
     * @throws IOException
     */
    private void renew(DatagramSocket socketDHCP) throws IOException {
        if (name != null) {
            byte[] buf = "renew".getBytes();
            DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, InetAddress.getLocalHost(), 7070); //create packet
            socketDHCP.send(sendPacket); //send packet
            byte[] bufRec = new byte[512];
            DatagramPacket recPacket = new DatagramPacket(bufRec, bufRec.length);
            socketDHCP.receive(recPacket); // receive Ack
            String ack = Utils.responseToString(recPacket.getData()).strip();
            System.out.println(ack);
        } else {
            System.out.println("Can't renew no IP exists");
            System.out.println("Getting New IP");
            DHCPConfig config = DHCP(socketDHCP);
            name = config.gateway;
            renewer.setIp(name);
        }

    }

    /**
     * @param socketDHCP Socket to connect to DHCP
     * @throws IOException
     */
    private void getIP(DatagramSocket socketDHCP) throws IOException {
        if (name == null) { //only run if no IP already assigned
            DHCPConfig config = DHCP(socketDHCP);
            name = config.gateway; //set name as IP
            renewer.setIp(name); //set name as IP
            System.out.println("New Ip Assigned");
        } else {
            System.out.println("already have IP can't get new one");
        }
    }
}
