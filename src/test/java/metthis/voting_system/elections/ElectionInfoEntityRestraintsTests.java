package metthis.voting_system.elections;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.TransactionSystemException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource("/test.properties")
// TestInstance.Lifecycle.PER_CLASS is selected to allow the use of @AfterAll on a non-static repository
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ElectionInfoEntityRestraintsTests {

    @Autowired
    private ElectionInfoRepository electionInfoRepository;

    @BeforeEach
    @AfterAll
    void deleteAll() {
        electionInfoRepository.deleteAll();
    }

    @Test
    void typeCannotBeNull() {
        ElectionInfo electionInfo = new ElectionInfo(null, "2023-11-01");
        assertThrows(TransactionSystemException.class, () -> {
            electionInfoRepository.save(electionInfo);
        });
    }

    @Test
    void dateCannotBeNull() {
        ElectionInfo electionInfo = new ElectionInfo(ElectionType.PRESIDENTIAL_ELECTION, null);
        assertThrows(TransactionSystemException.class, () -> {
            electionInfoRepository.save(electionInfo);
        });
    }

    @Test
    void votingRoundCannotBeNull() {
        ElectionInfo electionInfo = new ElectionInfo(ElectionType.PRESIDENTIAL_ELECTION, "2023-11-01");
        electionInfo.setVotingRound(null);
        assertThrows(TransactionSystemException.class, () -> {
            electionInfoRepository.save(electionInfo);
        });
    }
}
