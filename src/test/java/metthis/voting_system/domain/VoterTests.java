package metthis.voting_system.domain;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class VoterTests {
    private Voter voter;

    @BeforeEach
    void initEach() {
        this.voter = new Voter("Jane Doe", "12345678", "1995-11-27", true);
    }

    @Test
    void canGetName() {
        assertEquals("Jane Doe", this.voter.getName());
    }

    @Test
    void canGetID() {
        assertEquals("12345678", this.voter.getID());
    }

    @Test
    void getDateOfBirthIsInLocalDateFormat() {
        LocalDate expected = LocalDate.of(1995, 11, 27);
        assertEquals(expected, this.voter.getDateOfBirth());
    }

    @Test
    void canGetIsCitizen() {
        assertEquals(true, this.voter.getIsCitizen());
    }

    @Test
    void votedIsInitiallyFalse() {
        assertFalse(this.voter.getVoted());
    }

    @Test
    void votedVariableSetToTrueByVotedMethod() {
        this.voter.voted();
        assertTrue(this.voter.getVoted());
    }

}
