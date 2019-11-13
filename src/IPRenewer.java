import java.util.concurrent.ConcurrentHashMap;

public class IPRenewer extends Thread {
    public ConcurrentHashMap<Integer, IPRenew> renewHashMap;
    public Integer port;

    public IPRenewer(ConcurrentHashMap<Integer, IPRenew> renewHashMap, Integer port) {
        this.renewHashMap = renewHashMap;
        this.port = port;
    }

    @Override
    public void run() {
        renewHashMap.get(port).renew();
    }
}
