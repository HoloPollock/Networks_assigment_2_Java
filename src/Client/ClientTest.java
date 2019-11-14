package Client;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ClientTest {
    @Test
    public void clientTester() {
        ClientRunner client1 = new ClientRunner("www.cncf.io");
    }
}
