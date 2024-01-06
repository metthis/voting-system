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
public class VoterEntityRestraintsTests {

    @Autowired
    private VoterRepository voterRepository;

    @BeforeEach
    @AfterAll
    void deleteAll() {
        voterRepository.deleteAll();
    }

    @Test
    void nameCannotBeNull() {
        Voter voter = new Voter(null, "ID", "2020-01-01", true);
        assertThrows(TransactionSystemException.class, () -> {
            voterRepository.save(voter);
        });
    }

    @Test
    void IDMustBeManuallyAssigned() {
        Voter voter = new Voter("name", null, "2020-01-01", true);
        assertThrows(JpaSystemException.class, () -> {
            voterRepository.save(voter);
        });
    }

    @Test
    void dateOfBirthCannotBeNull() {
        Voter voter = new Voter("name", "ID", null, true);
        assertThrows(TransactionSystemException.class, () -> {
            voterRepository.save(voter);
        });
    }

    @Test
    void isCitizenCannotBeNull() {
        Voter voter = new Voter("name", "ID", "2020-01-01", null);
        assertThrows(TransactionSystemException.class, () -> {
            voterRepository.save(voter);
        });
    }

    // The following are tests of fields specific to Candidate:

    @Test
    void lastVotedRoundCannotBeNull() {
        Voter voter = new Voter("name", "ID", "2020-01-01", true);
        voter.setLastVotedRound(null);
        assertThrows(TransactionSystemException.class, () -> {
            voterRepository.save(voter);
        });
    }
}
