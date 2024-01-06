package metthis.voting_system.api;

import java.io.IOException;

public class TestUtils {
    byte[] fileToBytes(String fileName) throws IOException {
        return this.getClass().getResourceAsStream(fileName).readAllBytes();
    }

    String fileToString(String fileName) throws IOException {
        return new String(fileToBytes(fileName));
    }
}
