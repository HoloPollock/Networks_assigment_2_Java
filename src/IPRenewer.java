import java.util.concurrent.ConcurrentHashMap;

public class IPRenewer extends Thread {
    private ConcurrentHashMap<Integer, IPRenew> renewHashMap;
    private Integer port;

    IPRenewer(ConcurrentHashMap<Integer, IPRenew> renewHashMap, Integer port) {
        this.renewHashMap = renewHashMap;
        this.port = port;
    }

    @Override
    public void run() {
        renewHashMap.get(port).renew();
    }
}
