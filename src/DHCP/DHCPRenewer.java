package DHCP;

import inet.ipaddr.ipv4.IPv4Address;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class that checks for expired IP leases
 */
public class DHCPRenewer extends Thread {
    private ConcurrentHashMap<Integer, IPRenew> list; //list of in use IPs
    private IPList ipList; //all Ips possibles
    private Duration expiryTime; //time leases last
    private TimerTask repeatedTask = new TimerTask() { //Task to run automatically
        @Override
        public void run() {
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
            timer.scheduleAtFixedRate(repeatedTask, 0, 10000); // run task every 10 seconds
        } else {
            System.out.println("should be ran as daemon");
        }
    }

    /**
     * Funnction to check is a IP lease should be dropped
     */
    private void checkToDrop() {
        ArrayList<Integer> list_to_drop = new ArrayList<>();
        for (var entry : list.entrySet()) {
            var renew = entry.getValue();
            var port = entry.getKey();
            if (renew.IsExpired(expiryTime)) { //if expired drop
                list_to_drop.add(port);
            }
        }
        for (var i : list_to_drop) {
            drop(i);
            System.out.println("dropped" + i);
        }
    }

    private void drop(Integer drop) {
        ipList.dropUse((IPv4Address) list.get(drop).address); //but IP back in pool of possible IPs
        list.remove(drop); //remove from list of IPS tp be checked
    }

}
