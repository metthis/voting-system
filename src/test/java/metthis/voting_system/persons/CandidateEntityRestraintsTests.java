package metthis.voting_system.persons;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.TransactionSystemException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource("/test.properties")
// TestInstance.Lifecycle.PER_CLASS is selected to allow the use of @AfterAll on a non-static repository
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CandidateEntityRestraintsTests {

    @Autowired
    private CandidateRepository candidateRepository;

    @BeforeEach
    @AfterAll
    void deleteAll() {
        candidateRepository.deleteAll();
    }

    @Test
    void nameCannotBeNull() {
        Candidate candidate = new Candidate(null, "ID", "2020-01-01", true, "2022-01-01");
        assertThrows(TransactionSystemException.class, () -> {
            candidateRepository.save(candidate);
        });
    }

    @Test
    void IDMustBeManuallyAssigned() {
        Candidate candidate = new Candidate("name", null, "2020-01-01", true, "2022-01-01");
        assertThrows(JpaSystemException.class, () -> {
            candidateRepository.save(candidate);
        });
    }

    @Test
    void dateOfBirthCannotBeNull() {
        Candidate candidate = new Candidate("name", "ID", null, true, "2022-01-01");
        assertThrows(TransactionSystemException.class, () -> {
            candidateRepository.save(candidate);
        });
    }

    @Test
    void isCitizenCannotBeNull() {
        Candidate candidate = new Candidate("name", "ID", "2020-01-01", null, "2022-01-01");
        assertThrows(TransactionSystemException.class, () -> {
            candidateRepository.save(candidate);
        });
    }

    // The following are tests of fields specific to Candidate:

    @Test
    void registrationDateCannotBeNull() {
        Candidate candidate = new Candidate("name", "ID", "2020-01-01", true, null);
        assertThrows(TransactionSystemException.class, () -> {
            candidateRepository.save(candidate);
        });
    }

    @Test
    void withdrawalDateCanBeNull() {
        Candidate candidate = new Candidate("name", "ID", "2020-01-01", true, "2022-01-01");
        candidate.setWithdrawalDate(null);
        candidateRepository.save(candidate);
    }

    @Test
    void lostThisElectionCannotBeNull() {
        Candidate candidate = new Candidate("name", "ID", "2020-01-01", true, "2022-01-01");
        candidate.setLostThisElection(null);
        assertThrows(TransactionSystemException.class, () -> {
            candidateRepository.save(candidate);
        });
    }
}
