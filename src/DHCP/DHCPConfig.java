package DHCP;

import inet.ipaddr.IPAddress;
import inet.ipaddr.ipv4.IPv4Address;

import java.time.Duration;

public class DHCPConfig {
    public String ip;
    public String gateway;
    public String dnsPort;
    public Duration lease;

    public DHCPConfig(IPAddress ip, IPv4Address gateway) {
        this.ip = ip.toString();
        this.gateway = gateway.toString();
        this.dnsPort = "9090";
        this.lease = Duration.ofSeconds(60);
    }

    @Override
    public String toString() {
        return "DHCP.DHCPConfig{" +
                "ip='" + ip + '\'' +
                ", gateway='" + gateway + '\'' +
                ", dnsPort='" + dnsPort + '\'' +
                ", lease=" + lease +
                '}';
    }
}
