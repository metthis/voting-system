package metthis.voting_system.persons;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    @ParameterizedTest
    @CsvSource(textBlock = """
            2013-05-02,     17
            2013-05-03,     18
            2013-05-04,     18
            """)
    void getAgeReturnsCorrectAge(LocalDate date, int expectedAge) {
        int age = this.voter.getAge(date);

        assertEquals(expectedAge, age);
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
