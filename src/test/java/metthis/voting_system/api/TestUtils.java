package metthis.voting_system.api;

import java.io.IOException;

public class TestUtils {
    byte[] getBytesFromFile(String fileName) throws IOException {
        return this.getClass().getResourceAsStream(fileName).readAllBytes();
    }
}
