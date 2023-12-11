package metthis.voting_system.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import metthis.voting_system.persons.Candidate;

@JsonTest
public class CandidateJsonTests {

    @Autowired
    private JacksonTester<Candidate> json;

    @Autowired
    private JacksonTester<Candidate[]> jsonList;

    private static Candidate[] candidates;

    private static TestUtils testUtils;

    @BeforeAll
    static void initTestUtils() {
        testUtils = new TestUtils();
    }

    @BeforeAll
    static void initCandidates() {
        candidates = Arrays.array(
                new Candidate("John Smith", "123789", "1960-06-30", true, "2023-12-01"),
                new Candidate("Jane Doe", "7878002", "1972-01-01", false, "2023-12-02"));
        candidates[1].setWithdrawalDate("2023-12-10");
        candidates[1].setLostThisElection(true);
    }

    @Test
    void candidateIsSerializedCorrectly() throws IOException {
        assertThat(json.write(candidates[0])).isStrictlyEqualToJson("candidate.json");
    }

    @Test
    void candidateIsDesirializedCorrectly() throws IOException {
        byte[] actualBytes = testUtils.getBytesFromFile("candidate.json");
        assertThat(json.parse(actualBytes)).isEqualTo(candidates[0]);
    }

    @Test
    void candidateListIsSerializedCorrectly() throws IOException {
        assertThat(jsonList.write(candidates)).isStrictlyEqualToJson("candidateList.json");
    }

    @Test
    void candidateListIsDesirializedCorrectly() throws IOException {
        byte[] actualBytes = testUtils.getBytesFromFile("candidateList.json");
        assertThat(jsonList.parse(actualBytes)).isEqualTo(candidates);
    }
}
