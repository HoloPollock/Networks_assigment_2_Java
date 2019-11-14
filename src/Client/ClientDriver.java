package Client;

public class ClientDriver {

    public static void main(String[] args) {

//        ClientRunner runner = new ClientRunner("www.cncf.io", false);
        ClientRunner runner = new ClientRunner(true);
        runner.start();
    }
}
