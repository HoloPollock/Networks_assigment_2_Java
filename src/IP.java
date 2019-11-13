import inet.ipaddr.AddressStringException;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;
import inet.ipaddr.ipv4.IPv4Address;

import java.util.concurrent.atomic.AtomicBoolean;

public class IP {
    public IPv4Address ip;
    public AtomicBoolean inUse;

    public IP(String ip) throws AddressStringException {
        this.ip = (IPv4Address) new IPAddressString(ip).toAddress();
        this.inUse = new AtomicBoolean(false);
    }

    public IP(IPAddress ip) {
        this.ip = (IPv4Address) ip;
        this.inUse = new AtomicBoolean(false);

    }

    public boolean ipInUse() {
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
