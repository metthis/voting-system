package metthis.voting_system.persons;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/test.properties")
// TestInstance.Lifecycle.PER_CLASS is selected to allow the use of @AfterAll on a non-static repository
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CandidateRepositoryTests {

    @Autowired
    private CandidateRepository candidateRepository;

    @BeforeEach
    @AfterAll
    void deleteAll() {
        candidateRepository.deleteAll();
    }

    @Test
    void repositoryStartsEmpty() {
        assertEquals(0, this.candidateRepository.count());
    }

    @Test
    void isEmptyReturnsTrueWhenEmpty() {
        assertTrue(this.candidateRepository.isEmpty());
    }

    @Test
    void isEmptyReturnsFalseWhenOneCandidateWasAdded() {
        Candidate candidate = new Candidate("1", "1", "2000-01-01", true, "2023-01-01");
        this.candidateRepository.save(candidate);
        assertFalse(this.candidateRepository.isEmpty());
    }

    @Test
    void existsReturnsTrueWhenBothCandidatesAreTheSameObject() {
        Candidate candidate = new Candidate("1", "1", "2000-01-01", true, "2000-01-01");
        this.candidateRepository.save(candidate);

        assertTrue(this.candidateRepository.exists(candidate));
    }

    @Test
    void existsReturnsTrueWhenCandidatesHaveTheSameStateButAreDifferentObjects() {
        Candidate containedCandidate = new Candidate("1", "1", "2000-01-01", true, "2000-01-01");
        Candidate checkedCandidate = new Candidate("1", "1", "2000-01-01", true, "2000-01-01");
        this.candidateRepository.save(containedCandidate);

        assertTrue(this.candidateRepository.exists(checkedCandidate));
    }

    @Test
    void existsReturnsTrueWhenCandidatesOnlyShareID() {
        Candidate containedCandidate = new Candidate("1", "ID", "2000-01-01", true, "2000-01-01");
        Candidate checkedCandidate = new Candidate("2", "ID", "2020-12-31", false, "2020-12-31");
        this.candidateRepository.save(containedCandidate);

        assertTrue(this.candidateRepository.exists(checkedCandidate));
    }

    @Test
    void existsReturnsFalseWhenCandidatesHaveDifferentIDs() {
        Candidate containedCandidate = new Candidate("name", "1", "2000-01-01", true, "2000-01-01");
        Candidate checkedCandidate = new Candidate("name", "2", "2000-01-01", true, "2000-01-01");
        this.candidateRepository.save(containedCandidate);

        assertFalse(this.candidateRepository.exists(checkedCandidate));
    }

    // The following are tests of methods specific to CandidateRepository:

    @Test
    void howManyWithdrewReturnsCorrectNumber() {
        int EXPECTED_NUMBER_OF_WITHDRAWN_CANDIDATES = 2;

        Candidate firstCandidate = new Candidate("1", "1", "2000-01-01", true, "2023-01-01");
        Candidate secondCandidate = new Candidate("2", "2", "2020-12-12", true, "2023-01-01");
        Candidate thirdCandidate = new Candidate("3", "3", "2022-12-12", true, "2023-01-01");

        firstCandidate.setWithdrawalDate("2023-02-01");
        secondCandidate.setWithdrawalDate("2023-02-01");

        this.candidateRepository.save(firstCandidate);
        this.candidateRepository.save(secondCandidate);
        this.candidateRepository.save(thirdCandidate);

        int withdrawnCandidates = this.candidateRepository.howManyWithdrew();

        assertEquals(EXPECTED_NUMBER_OF_WITHDRAWN_CANDIDATES, withdrawnCandidates);
    }
}
