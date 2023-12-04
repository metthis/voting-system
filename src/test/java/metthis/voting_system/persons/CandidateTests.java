package metthis.voting_system.persons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class CandidateTests {
    private Candidate candidate;

    @Nested
    class New {
        @Test
        void isInitializedWithDefaultConstructor() {
            new Candidate();
        }

        @Test
        void isInitializedWithConstructorWithArguments() {
            new Candidate("Jane Doe", "12345678", "1995-05-03", true, "2023-03-15");
        }
    }

    @Nested
    class WhenNew {
        @BeforeEach
        void initEach() {
            candidate = new Candidate("Jane Doe", "12345678", "1995-05-03", true, "2023-03-15");
        }

        @Test
        void canGetName() {
            assertEquals("Jane Doe", candidate.getName());
        }

        @Test
        void canGetID() {
            assertEquals("12345678", candidate.getID());
        }

        @Test
        void getDateOfBirthIsInLocalDateFormat() {
            LocalDate expected = LocalDate.of(1995, 05, 03);
            assertEquals(expected, candidate.getDateOfBirth());
        }

        @Test
        void canGetIsCitizen() {
            assertEquals(true, candidate.getIsCitizen());
        }

        @ParameterizedTest
        @CsvSource(textBlock = """
                2013-05-02,     17
                2013-05-03,     18
                2013-05-04,     18
                """)
        void getAgeReturnsCorrectAge(LocalDate date, int expectedAge) {
            int age = candidate.getAge(date);

            assertEquals(expectedAge, age);
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

        // The following are tests specific to Candidate:

        @Test
        void getRegistrationDateIsInLocalDateFormat() {
            LocalDate expected = LocalDate.of(2023, 03, 15);
            assertEquals(expected, candidate.getRegistrationDate());
        }

        @Test
        void canGetwithdrawalDate() {
            candidate.getWithdrawalDate();
        }

        @Test
        void withdrawalDateCanBeSetToLocalDateFormat() {
            candidate.withdraw("2023-10-31");
            LocalDate expected = LocalDate.of(2023, 10, 31);
            assertEquals(expected, candidate.getWithdrawalDate());
        }

        @Test
        void canGetlostThisElection() {
            candidate.getLostThisElection();
        }

        @Test
        void lostThisElectionCanBeSetToTrue() {
            candidate.setLostThisElection(true);
            assertTrue(candidate.getLostThisElection());
        }

        @Test
        void lostThisElectionCanBeSetBackToFalse() {
            candidate.setLostThisElection(true);
            candidate.setLostThisElection(false);
            assertFalse(candidate.getLostThisElection());
        }
    }

    @Nested
    class DefaultFieldValues {

        @BeforeEach
        void initWithTheCorrectConstructor(RepetitionInfo repetitionInfo) {
            int repetition = repetitionInfo.getCurrentRepetition();
            if (repetition == 1) {
                candidate = new Candidate();
            } else if (repetition == 2) {
                candidate = new Candidate("Jane Doe", "12345678", "1995-05-03", true, "2023-03-15");
            }
        }

        // The following are tests specific to Candidate:

        @RepeatedTest(2)
        void withdrawalDateIsInitiallyNull() {
            assertNull(candidate.getWithdrawalDate());
        }

        @RepeatedTest(2)
        void lostThisElectionIsInitiallyFalse() {
            assertFalse(candidate.getLostThisElection());
        }

    }
}
