package metthis.voting_system.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import metthis.voting_system.persons.Candidate;

@JsonTest
public class CandidateJsonTests {

    @Autowired
    private JacksonTester<Candidate> json;

    @Autowired
    private JacksonTester<Candidate[]> jsonList;

    private Candidate[] candidates;

    @BeforeEach
    void initCandidates() {
        candidates = Arrays.array(
                new Candidate("John Smith", "123789", "1960-06-30", true, "2023-12-01"),
                new Candidate("Jane Doe", "7878002", "1972-01-01", false, "2023-12-02"));
    }

    @Test
    void candidateIsSerializedCorrectly() throws IOException {
        JsonContent<Candidate> candidateJson = json.write(candidates[0]);
        assertThat(candidateJson).isStrictlyEqualToJson("candidate.json");
    }

}
