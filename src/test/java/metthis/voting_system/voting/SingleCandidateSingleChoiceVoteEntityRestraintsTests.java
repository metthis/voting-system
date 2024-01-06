package metthis.voting_system.voting;

import metthis.voting_system.persons.Candidate;
import metthis.voting_system.persons.CandidateRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.TransactionSystemException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource("/test.properties")
// TestInstance.Lifecycle.PER_CLASS is selected to allow the use of @BeforeAll on a non-static variable
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SingleCandidateSingleChoiceVoteEntityRestraintsTests {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    private Candidate candidate;

    @BeforeAll
    void initCandidate() {
        candidate = new Candidate("name", "ID", "2020-01-01", true, "2022-01-01");
    }

    @BeforeEach
    @AfterAll
    void deleteAll() {
        voteRepository.deleteAll();
        candidateRepository.deleteAll();
    }

    @Test
    void votingRoundCannotBeNull() {
        // The candidate has to be saved so that it can be used as a choice in a vote
        candidateRepository.save(candidate);

        Vote vote = new SingleCandidateSingleChoiceVote(
                null, candidate);
        assertThrows(TransactionSystemException.class, () -> {
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
