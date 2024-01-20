package metthis.voting_system.elections;

import metthis.voting_system.persons.Candidate;
import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ElectionInfoTests {
    private Candidate candidate;

    @Nested
    class New {
        @Test
        void isInitializedWithConstructorWithArguments() {
            new ElectionInfo(ElectionType.PRESIDENTIAL_ELECTION, "2023-12-01");
        }
    }

    @Nested
    class WhenNew {
        private ElectionInfo electionInfo;

        @BeforeEach
        void initEach() {
            electionInfo = new ElectionInfo(ElectionType.PRESIDENTIAL_ELECTION, "2023-12-01");
        }

        @Test
        void canGetType() {
            assertEquals(ElectionType.PRESIDENTIAL_ELECTION, electionInfo.getType());
        }

        @Test
        void canGetDate() {
            LocalDate expected = LocalDate.of(2023, 12, 1);
            assertEquals(expected, electionInfo.getDate());
        }

        @Test
        void canGetVotingRound() {
            assertEquals(0, electionInfo.getVotingRound());
        }

        @Test
        void canSetVotingRound() {
            electionInfo.setVotingRound(2);
            assertEquals(2, electionInfo.getVotingRound());
        }
    }
}
