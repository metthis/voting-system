package metthis.voting_system.persons;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("/test.properties")
@EntityScan({ "metthis.voting_system.persons" })
public class VoterEntityRestraintsTests {

    @Autowired
    VoterRepository voterRepository;

    @AfterEach
    void deleteAll() {
        voterRepository.deleteAll();
    }

    @Test
    void nameCannotBeNull() {
        Voter voter = new Voter(null, "ID", "2020-01-01", true);
        assertThrows(DataIntegrityViolationException.class, () -> {
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
        assertThrows(DataIntegrityViolationException.class, () -> {
            voterRepository.save(voter);
        });
    }

    @Test
    void isCitizenCannotBeNull() {
        Voter voter = new Voter("name", "ID", "2020-01-01", null);
        assertThrows(DataIntegrityViolationException.class, () -> {
            voterRepository.save(voter);
        });
    }

    // The following are tests of fields specific to Candidate:

    @Test
    void lastVotedRoundCannotBeNull() {
        Voter voter = new Voter("name", "ID", "2020-01-01", true);
        voter.setLastVotedRound(null);
        assertThrows(DataIntegrityViolationException.class, () -> {
            voterRepository.save(voter);
        });
    }
}
