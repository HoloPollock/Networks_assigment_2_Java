package Client;

public class ClientDriver {

    public static void main(String[] args) {

        ClientRunner runner = new ClientRunner("www.cncf.io");
        runner.start();
    }
}
