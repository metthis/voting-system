package metthis.voting_system.persons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class CandidateTests {
    private Candidate candidate;

    @BeforeEach
    void initEach() {
        this.candidate = new Candidate("Jane Doe", "12345678", "1995-05-03", true, "2023-03-15");
    }

    @Test
    void canGetName() {
        assertEquals("Jane Doe", this.candidate.getName());
    }

    @Test
    void canGetID() {
        assertEquals("12345678", this.candidate.getID());
    }

    @Test
    void getDateOfBirthIsInLocalDateFormat() {
        LocalDate expected = LocalDate.of(1995, 05, 03);
        assertEquals(expected, this.candidate.getDateOfBirth());
    }

    @Test
    void canGetIsCitizen() {
        assertEquals(true, this.candidate.getIsCitizen());
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
            2013-05-02,     17
            2013-05-03,     18
            2013-05-04,     18
            """)
    void getAgeReturnsCorrectAge(LocalDate date, int expectedAge) {
        int age = this.candidate.getAge(date);

        assertEquals(expectedAge, age);
    }

    @Disabled("Disabled until Election.isEligible methods get their tests")
    @Test
    void isEligible() {

    }

    @Test
    void equalsReturnsTrueWhenAllFieldsAreSame() {
        Candidate candidate1 = new Candidate("name", "IDid456", "2020-01-01", false, "2020-01-01");
        Candidate candidate2 = new Candidate("name", "IDid456", "2020-01-01", false, "2020-01-01");

        assertTrue(candidate1.equals(candidate2));
    }

    @Test
    void equalsReturnsTrueWhenOnlyIDsAreSame() {
        Candidate candidate1 = new Candidate("name1", "IDid456", "2020-01-01", false, "2021-01-01");
        Candidate candidate2 = new Candidate("name1", "IDid456", "1920-12-31", true, "1921-12-31");

        assertTrue(candidate1.equals(candidate2));
    }

    @Test
    void equalsReturnsFalseWhenIDsAreDifferent() {
        Candidate candidate1 = new Candidate("name", "IDid456", "2020-01-01", false, "2020-01-01");
        Candidate candidate2 = new Candidate("name", "foobar", "2020-01-01", false, "2020-01-01");

        assertFalse(candidate1.equals(candidate2));
    }

    @Test
    void equalsReturnsTrueWhenBothIDsAreNull() {
        Candidate candidate1 = new Candidate("name", null, "2020-01-01", false, "2020-01-01");
        Candidate candidate2 = new Candidate("name", null, "2020-01-01", false, "2020-01-01");

        assertTrue(candidate1.equals(candidate2));
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
            1,  2,  -1.0
            2,  1,  1.0
            1,  1,  0
            """)
    void compareToReturnsCorrectlySignedInt(String ID1, String ID2, double expected) {
        Candidate candidate1 = new Candidate("", ID1, "2000-01-01", false, "2020-01-01");
        Candidate candidate2 = new Candidate("", ID2, "2000-01-01", false, "2020-01-01");

        double actual = Math.signum((double) candidate1.compareTo(candidate2));

        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource(textBlock = """
            1,      1
            1,      2
            1,      a
            a,      b
            B,      a
            a,      aa
            """)
    void sortingWorksCorrectly(String smallerID, String largerID) {
        Candidate smallerCandidate = new Candidate("", smallerID, "2000-01-01", false, "2020-01-01");
        Candidate largerCandidate = new Candidate("", largerID, "2000-01-01", false, "2020-01-01");

        Candidate[] resultedOrder = { smallerCandidate, largerCandidate };
        Arrays.sort(resultedOrder);

        Candidate[] resultedOrderInputsFlipped = { largerCandidate, smallerCandidate };
        Arrays.sort(resultedOrderInputsFlipped);

        assertEquals(smallerID, resultedOrder[0].getID(), resultedOrderInputsFlipped[0].getID());
        assertEquals(largerID, resultedOrder[1].getID(), resultedOrderInputsFlipped[1].getID());
    }

    // The following are tests of methods specific to Candidate:

    @Test
    void getApplicationDateIsInLocalDateFormat() {
        LocalDate expected = LocalDate.of(2023, 03, 15);
        assertEquals(expected, this.candidate.getAplicationDate());
    }

    @Test
    void withdrawalDateIsInitiallyNull() {
        assertNull(this.candidate.getWithdrawalDate());
    }

    @Test
    void withdrawalDateCanBeSetToLocalDateFormat() {
        this.candidate.withdraw("2023-10-31");
        LocalDate expected = LocalDate.of(2023, 10, 31);
        assertEquals(expected, this.candidate.getWithdrawalDate());
    }

    @Test
    void lostThisElectionIsInitiallyFalse() {
        assertFalse(this.candidate.getLostThisElection());
    }

    @Test
    void lostThisElectionCanBeSetToTrue() {
        this.candidate.setLostThisElection(true);
        assertTrue(this.candidate.getLostThisElection());
    }

    @Test
    void lostThisElectionCanBeSetBackToFalse() {
        this.candidate.setLostThisElection(true);
        this.candidate.setLostThisElection(false);
        assertFalse(this.candidate.getLostThisElection());
    }
}
