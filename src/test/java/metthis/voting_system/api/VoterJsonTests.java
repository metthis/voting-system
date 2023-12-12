package metthis.voting_system.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import metthis.voting_system.persons.Voter;

@JsonTest
public class VoterJsonTests {
    @Autowired
    private JacksonTester<Voter> json;

    @Autowired
    private JacksonTester<Voter[]> jsonList;

    private static Voter[] voters;

    private static TestUtils testUtils;

    @BeforeAll
    static void initTestUtils() {
        testUtils = new TestUtils();
    }

    @BeforeAll
    static void initVoters() {
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
