import inet.ipaddr.IPAddress;

import java.time.Duration;
import java.time.Instant;

public class IPRenew {
    IPAddress address;
    Instant timeRenewed;

    public IPRenew(IPAddress address, Instant timeRenewed) {
        this.address = address;
        this.timeRenewed = timeRenewed;
    }

    public boolean IsExpired(Duration time) {
        var temp = Duration.between(timeRenewed, Instant.now());
        var compareTo = temp.compareTo(time);
        return compareTo > 0;
    }

    public void renew() {
        timeRenewed = Instant.now();
    }

    @Override
    public String toString() {
        return "IPRenew{" +
                "address=" + address +
                ", timeRenewed=" + timeRenewed +
                '}';
    }
}
