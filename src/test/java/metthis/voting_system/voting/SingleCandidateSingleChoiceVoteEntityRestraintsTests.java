package metthis.voting_system.voting;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import metthis.voting_system.persons.Candidate;

@SpringBootTest
@TestPropertySource("/test.properties")
public class SingleCandidateSingleChoiceVoteEntityRestraintsTests {

    @Autowired
    @Qualifier("singleCandidateSingleChoiceVoteRepository")
    private SingleCandidateSingleChoiceVoteRepository voteRepository;

    @AfterEach
    void deleteAll() {
        voteRepository.deleteAll();
    }

    @Test
    void votingRoundCannotBeNull() {
        Candidate candidate = new Candidate("name", "ID", "2020-01-01", null, "2022-01-01");
        SingleCandidateSingleChoiceVote vote = new SingleCandidateSingleChoiceVote(
                null, candidate);
        assertThrows(DataIntegrityViolationException.class, () -> {
            voteRepository.save(vote);
        });
    }

    @Test
    void choiceCanBeNull() {
        SingleCandidateSingleChoiceVote vote = new SingleCandidateSingleChoiceVote(
                1, null);
        voteRepository.save(vote);
    }
}
