package Client;

public class ClientDriver {

    public static void main(String[] args) {

        ClientRunner runner2 = new ClientRunner("www.cncf.io", true);
//        ClientRunner runner = new ClientRunner("www.linuxfoundation.org",false);
       runner2.start();
//       runner.start();
    }
}
