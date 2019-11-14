package models;

public class ClientPacket {
    String gatewayMac;
    String mac;
    String siteIp;
    String Ip;
    int dstPort;
    int srcPort;
    String appData;

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
        return "ClientPacket{\n" +
                "gatewayMac='" + gatewayMac + '\'' +
                ",\n mac='" + mac + '\'' +
                ",\n siteIp='" + siteIp + '\'' +
                ",\n Ip='" + Ip + '\'' +
                ",\n dstPort=" + dstPort +
                ",\n srcPort=" + srcPort +
                ",\n appData='" + appData + '\'' + "\n" +
                '}';
    }
}
