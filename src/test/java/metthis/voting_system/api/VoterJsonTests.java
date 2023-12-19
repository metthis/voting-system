package metthis.voting_system.api;

import metthis.voting_system.persons.Voter;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
// TestInstance.Lifecycle.PER_CLASS is selected to allow the use of @BeforeAll on non-static variables
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VoterJsonTests {
    @Autowired
    private JacksonTester<Voter> json;

    @Autowired
    private JacksonTester<Voter[]> jsonList;

    private Voter[] voters;

    private TestUtils testUtils;

    @BeforeAll
    void initTestUtils() {
        testUtils = new TestUtils();
    }

    @BeforeAll
    void initVoters() {
        voters = Arrays.array(
                new Voter("John Smith", "123789", "1960-06-30", true),
                new Voter("Jane Doe", "7878002", "1972-01-01", false));
        voters[1].setLastVotedRound(2);
    }

    @Test
    void voterIsSerializedCorrectly() throws IOException {
        assertThat(json.write(voters[0])).isStrictlyEqualToJson("voter.json");
    }

    @Test
    void voterIsDesirializedCorrectly() throws IOException {
        byte[] actualBytes = testUtils.fileToBytes("voter.json");
        assertThat(json.parse(actualBytes))
                .usingRecursiveComparison()
                .isEqualTo(voters[0]);
    }

    @Test
    void voterListIsSerializedCorrectly() throws IOException {
        assertThat(jsonList.write(voters)).isStrictlyEqualToJson("voterList.json");
    }

    @Test
    void voterListIsDesirializedCorrectly() throws IOException {
        byte[] actualBytes = testUtils.fileToBytes("voterList.json");
        assertThat(jsonList.parse(actualBytes))
                .usingRecursiveComparison()
                .isEqualTo(voters);
    }
}
