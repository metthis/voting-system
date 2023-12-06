package metthis.voting_system.persons;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("/test.properties")
public class CandidateEntityRestraintsTests {

    @Autowired
    CandidateRepository candidateRepository;

    @AfterEach
    void deleteAll() {
        candidateRepository.deleteAll();
    }

    @Test
    void nameCannotBeNull() {
        Candidate candidate = new Candidate(null, "ID", "2020-01-01", true, "2022-01-01");
        assertThrows(DataIntegrityViolationException.class, () -> {
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
        assertThrows(DataIntegrityViolationException.class, () -> {
            candidateRepository.save(candidate);
        });
    }

    @Test
    void isCitizenCannotBeNull() {
        Candidate candidate = new Candidate("name", "ID", "2020-01-01", null, "2022-01-01");
        assertThrows(DataIntegrityViolationException.class, () -> {
            candidateRepository.save(candidate);
        });
    }

    // The following are tests of fields specific to Candidate:

    @Test
    void registrationDateCannotBeNull() {
        Candidate candidate = new Candidate("name", "ID", "2020-01-01", true, null);
        assertThrows(DataIntegrityViolationException.class, () -> {
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
        assertThrows(DataIntegrityViolationException.class, () -> {
            candidateRepository.save(candidate);
        });
    }
}
