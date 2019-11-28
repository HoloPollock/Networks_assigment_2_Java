package models;

public class ClientPacket {
    private String gatewayMac;
    private String mac;
    private String siteIp;
    private String Ip;
    private int dstPort;
    private int srcPort;
    private String appData;

    public ClientPacket(String siteIp, String ip, int srcPort, String appData) {
        this.gatewayMac = "00:1b:21:36:61:13";
        this.mac = "78:4f:43:55:b1:69";
        this.siteIp = siteIp;
        Ip = ip;
        this.dstPort = 80;
        this.srcPort = srcPort;
        this.appData = appData;
    }

    @Override
    public String toString() {
        return "|" + gatewayMac + '|' + mac + "|" + siteIp + "|" + Ip + "|" + dstPort + "|" + srcPort + "|" + appData + "|";

    }
}
