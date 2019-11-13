import inet.ipaddr.AddressStringException;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;
import inet.ipaddr.ipv4.IPv4Address;

import java.util.concurrent.atomic.AtomicBoolean;

public class IP {
    IPv4Address ip;
    AtomicBoolean inUse;

    public IP(String ip) throws AddressStringException {
        this.ip = (IPv4Address) new IPAddressString(ip).toAddress();
        this.inUse = new AtomicBoolean(false);
    }

    IP(IPAddress ip) {
        this.ip = (IPv4Address) ip;
        this.inUse = new AtomicBoolean(false);

    }

    boolean ipInUse() {
        return this.inUse.get();
    }

    @Override
    public String toString() {
        return "IP{" +
                "ip=" + ip +
                ", inUse=" + inUse +
                '}';
    }
}
