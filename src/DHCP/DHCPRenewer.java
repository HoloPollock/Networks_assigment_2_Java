package DHCP;

import inet.ipaddr.ipv4.IPv4Address;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class DHCPRenewer extends Thread {
    private ConcurrentHashMap<Integer, IPRenew> list;
    private IPList ipList;
    private Duration expiryTime;
    private TimerTask repeatedTask = new TimerTask() {
        @Override
        public void run() {
            list.forEach((key, value) -> System.out.print("{" + key + " " + value + "}"));
            System.out.println();
            checkToDrop();
        }
    };

    DHCPRenewer(ConcurrentHashMap<Integer, IPRenew> list, IPList ipList, Duration expiryTime) {
        this.list = list;
        this.ipList = ipList;
        this.expiryTime = expiryTime;
    }

    @Override
    public void run() {
        if (Thread.currentThread().isDaemon()) {
            Timer timer = new Timer("runner");
            timer.scheduleAtFixedRate(repeatedTask, 0, 10000);
        } else {
            System.out.println("should be ran as daemon");
        }
    }

    private void checkToDrop() {
        ArrayList<Integer> list_to_drop = new ArrayList<>();
        for (var entry : list.entrySet()) {
            var renew = entry.getValue();
            var port = entry.getKey();
            if (renew.IsExpired(expiryTime)) {
                list_to_drop.add(port);
            }
        }
        for (var i : list_to_drop) {
            drop(i);
            System.out.println("dropped" + i);
        }
    }

    private void drop(Integer drop) {
        ipList.dropUse((IPv4Address) list.get(drop).address);
        list.remove(drop);
    }

}
