package DHCP;

import inet.ipaddr.ipv4.IPv4Address;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Effectively a type alias of ArrayList<IP> with some extra functionality
 *
 */
public class IPList extends ArrayList<IP> {
    IPList() {
        super();
    }

    /**
     * @return Optional of Ip address to use as there may be none left
     * get first IP available from list of IPs
     */
    Optional<IPv4Address> getFirstUnused() {
        for (IP ip : this) {
            if (!ip.ipInUse()) {
                IPv4Address addr = ip.ip;
                ip.inUse.set(true);
                return Optional.of(addr);
            }
        }
        return Optional.empty();
    }

    /**
     * @param ip Ip to drop
     * When the term drop is used it mean add back to bool to be used (Switch boolean to false)
     */
    void dropUse(IPv4Address ip) {
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
