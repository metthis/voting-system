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
import metthis.voting_system.voting.SingleCandidateSingleChoiceVote;

@JsonTest
public class SingleCandidateSingleChoiceVoteJsonTests {
    @Autowired
    private JacksonTester<SingleCandidateSingleChoiceVote> json;

    @Autowired
    private JacksonTester<SingleCandidateSingleChoiceVote[]> jsonList;

    private static SingleCandidateSingleChoiceVote[] votesToSerialize;

    private static SingleCandidateSingleChoiceVote[] votesDeserialized;

    private static TestUtils testUtils;

    @BeforeAll
    static void initTestUtils() {
        testUtils = new TestUtils();
    }

    @BeforeAll
    static void initCandidatesAndVotes() {
        Candidate candidateToSerialize = new Candidate("John Smith", "123789", "1960-06-30", true, "2023-12-01");
        votesToSerialize = Arrays.array(
                new SingleCandidateSingleChoiceVote(
                        1,
                        candidateToSerialize),
                new SingleCandidateSingleChoiceVote(
                        3,
                        null));

        Candidate candidateDeserialized = new Candidate(null, "123789", null, null, null);
        votesDeserialized = Arrays.array(
                new SingleCandidateSingleChoiceVote(
                        1,
                        candidateDeserialized),
                votesToSerialize[1]);
    }

    @Test
    void voteIsSerializedCorrectly() throws IOException {
        assertThat(json.write(votesToSerialize[0]))
                .isStrictlyEqualToJson("singleCandidateSingleChoiceVote.json");
    }

    @Test
    void voteIsDesirializedCorrectly() throws IOException {
        byte[] actualBytes = testUtils
                .fileToBytes("singleCandidateSingleChoiceVote.json");
        assertThat(json.parse(actualBytes))
                .usingRecursiveComparison()
                .isEqualTo(votesDeserialized[0]);
    }

    @Test
    void voteListIsSerializedCorrectly() throws IOException {
        assertThat(jsonList.write(votesToSerialize))
                .isStrictlyEqualToJson("singleCandidateSingleChoiceVoteList.json");
    }

    @Test
    void voteListIsDesirializedCorrectly() throws IOException {
        byte[] actualBytes = testUtils
                .fileToBytes("singleCandidateSingleChoiceVoteList.json");
        assertThat(jsonList.parse(actualBytes))
                .usingRecursiveComparison()
                .isEqualTo(votesDeserialized);
    }
}
