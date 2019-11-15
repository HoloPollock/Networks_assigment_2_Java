package DHCP;

import inet.ipaddr.IPAddress;

import java.time.Duration;
import java.time.Instant;

public class IPRenew {
    IPAddress address;
    private Instant timeRenewed;

    IPRenew(IPAddress address, Instant timeRenewed) {
        this.address = address;
        this.timeRenewed = timeRenewed;
    }

    /**
     * @param time duration of expiry time
     * @return if the IPs lease has expired
     * checks to see if the time since last renew is greater than the expiry time from now
     */
    boolean IsExpired(Duration time) {
        var temp = Duration.between(timeRenewed, Instant.now());
        var compareTo = temp.compareTo(time);
        return compareTo > 0;
    }

    void renew() {
        timeRenewed = Instant.now();
    }

    @Override
    public String toString() {
        return "DHCP.IPRenew{" +
                "address=" + address +
                ", timeRenewed=" + timeRenewed +
                '}';
    }
}
