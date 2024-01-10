package metthis.voting_system.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public class TestUtils {

    private ObjectMapper objectMapper = new ObjectMapper();

    byte[] fileToBytes(String fileName) throws IOException {
        return this.getClass().getResourceAsStream(fileName).readAllBytes();
    }

    String fileToString(String fileName) throws IOException {
        return new String(fileToBytes(fileName));
    }


    // Following overloads differ only by the type of newValue, Object can't be used as a type
    String modifyJsonField(String json, String field, String newValue)
            throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(json);
        ((ObjectNode) jsonNode).put(field, newValue);
        return objectMapper.writeValueAsString(jsonNode);
    }

    String modifyJsonField(String json, String field, Boolean newValue)
            throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(json);
        ((ObjectNode) jsonNode).put(field, newValue);
        return objectMapper.writeValueAsString(jsonNode);
    }
}
