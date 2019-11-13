import inet.ipaddr.ipv4.IPv4Address;

import java.util.ArrayList;
import java.util.Optional;

public class IpList extends ArrayList<IP> {
    public IpList() {
        super();
    }

    public Optional<IPv4Address> getFirstUnused() {
        for (IP ip : this) {
            if (!ip.ipInUse()) {
                IPv4Address addr = ip.ip;
                ip.inUse.set(true);
                return Optional.of(addr);
            }
        }
        return Optional.empty();
    }

    public void dropUse(IPv4Address ip) {
        for (IP i : this) {
            if (i.ip.equals(ip)) {
                i.inUse.set(false);
            }
        }
    }

    @Override
    public int indexOf(Object o) {
        return super.indexOf(o);
    }

    @Override
    public IP get(int index) {
        return super.get(index);
    }
}
