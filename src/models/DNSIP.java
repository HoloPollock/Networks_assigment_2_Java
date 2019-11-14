package models;

import inet.ipaddr.ipv4.IPv4Address;
import inet.ipaddr.ipv6.IPv6Address;

public class DNSIP {
    String IPv4;
    String IPv6;

   public DNSIP(IPv4Address iPv4, IPv6Address iPv6) {
       this.IPv6 = iPv6.toString();
       this.IPv4 = iPv4.toString();
   }

    public DNSIP(String IPv4, String IPv6) {
        this.IPv4 = IPv4;
        this.IPv6 = IPv6;
    }

    @Override
    public String toString() {
        return "DNSIP{" +
                "IPv4='" + IPv4 + '\'' +
                ", IPv6='" + IPv6 + '\'' +
                '}';
    }
}
