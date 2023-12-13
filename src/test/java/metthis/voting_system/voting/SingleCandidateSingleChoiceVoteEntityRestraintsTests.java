package metthis.voting_system.voting;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import metthis.voting_system.persons.Candidate;
import metthis.voting_system.persons.CandidateRepository;

@SpringBootTest
@TestPropertySource("/test.properties")
public class SingleCandidateSingleChoiceVoteEntityRestraintsTests {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    private static Candidate candidate;

    @BeforeAll
    static void initCandidate() {
        candidate = new Candidate("name", "ID", "2020-01-01", true, "2022-01-01");
    }

    @AfterEach
    void deleteAll() {
        voteRepository.deleteAll();
        candidateRepository.deleteAll();
    }

    @Test
    void votingRoundCannotBeNull() {
        Vote vote = new SingleCandidateSingleChoiceVote(
                null, candidate);
        assertThrows(DataIntegrityViolationException.class, () -> {
            voteRepository.save(vote);
        });
    }

    @Test
    void choiceCanBeNull() {
        Vote vote = new SingleCandidateSingleChoiceVote(
                1, null);
        voteRepository.save(vote);
    }

    @Test
    void multipleVotesCanHaveTheSameChoice() {
        // The candidate has to be saved so that it can be used as a choice in a vote
        candidateRepository.save(candidate);

        for (int i = 0; i < 3; i++) {
            Vote vote = new SingleCandidateSingleChoiceVote(1, candidate);
            voteRepository.save(vote);
        }
    }
}
