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
        this.voter = new Voter("Jane Doe", "12345678", "1995-05-03", true);
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
        LocalDate expected = LocalDate.of(1995, 05, 03);
        assertEquals(expected, this.voter.getDateOfBirth());
    }

    @Test
    void canGetIsCitizen() {
        assertEquals(true, this.voter.getIsCitizen());
    }

    @Test // Should be parametrised
    void getAgeReturnsCorrectAge() {
        LocalDate date1 = LocalDate.of(2013, 05, 02);
        LocalDate date2 = LocalDate.of(2013, 05, 03);
        LocalDate date3 = LocalDate.of(2013, 05, 04);

        int age1 = this.voter.getAge(date1);
        int age2 = this.voter.getAge(date2);
        int age3 = this.voter.getAge(date3);

        assertEquals(17, age1);
        assertEquals(18, age2);
        assertEquals(18, age3);
    }

    // The following are tests of methods specific to Voter:

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
