package metthis.voting_system.persons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
import org.junit.jupiter.params.provider.ValueSource;

public class VoterTests {
    private Voter voter;

    @Nested
    class New {
        @Test
        void isInitializedWithDefaultConstructor() {
            new Voter();
        }

        @Test
        void isInitializedWithConstructorWithArguments() {
            new Voter("Jane Doe", "12345678", "1995-05-03", true);
        }
    }

    @Nested
    class WhenNew {
        @BeforeEach
        void initEach() {
            voter = new Voter("Jane Doe", "12345678", "1995-05-03", true);
        }

        @Test
        void canGetName() {
            assertEquals("Jane Doe", voter.getName());
        }

        @Test
        void canGetID() {
            assertEquals("12345678", voter.getID());
        }

        @Test
        void getDateOfBirthIsInLocalDateFormat() {
            LocalDate expected = LocalDate.of(1995, 05, 03);
            assertEquals(expected, voter.getDateOfBirth());
        }

        @Test
        void canGetIsCitizen() {
            assertEquals(true, voter.getIsCitizen());
        }

        @ParameterizedTest
        @CsvSource(textBlock = """
                2013-05-02,     17
                2013-05-03,     18
                2013-05-04,     18
                """)
        void getAgeReturnsCorrectAge(LocalDate date, int expectedAge) {
            int age = voter.getAge(date);

            assertEquals(expectedAge, age);
        }

        @Test
        void equalsReturnsTrueWhenAllFieldsAreSame() {
            Voter voter1 = new Voter("name", "IDid456", "2020-01-01", false);
            Voter voter2 = new Voter("name", "IDid456", "2020-01-01", false);

            assertTrue(voter1.equals(voter2));
        }

        @Test
        void equalsReturnsTrueWhenOnlyIDsAreSame() {
            Voter voter1 = new Voter("name1", "IDid456", "2020-01-01", false);
            Voter voter2 = new Voter("name1", "IDid456", "1920-12-31", true);

            assertTrue(voter1.equals(voter2));
        }

        @Test
        void equalsReturnsFalseWhenIDsAreDifferent() {
            Voter voter1 = new Voter("name", "IDid456", "2020-01-01", false);
            Voter voter2 = new Voter("name", "foobar", "2020-01-01", false);

            assertFalse(voter1.equals(voter2));
        }

        @ParameterizedTest
        @CsvSource(textBlock = """
                1,  2,  -1.0
                2,  1,  1.0
                1,  1,  0
                """)
        void compareToReturnsCorrectlySignedInt(String ID1, String ID2, double expected) {
            Voter voter1 = new Voter("", ID1, "2000-01-01", false);
            Voter voter2 = new Voter("", ID2, "2000-01-01", false);

            double actual = Math.signum((double) voter1.compareTo(voter2));

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
            Voter smallerVoter = new Voter("", smallerID, "2000-01-01", false);
            Voter largerVoter = new Voter("", largerID, "2000-01-01", false);

            Voter[] resultedOrder = { smallerVoter, largerVoter };
            Arrays.sort(resultedOrder);

            Voter[] resultedOrderInputsFlipped = { largerVoter, smallerVoter };
            Arrays.sort(resultedOrderInputsFlipped);

            assertEquals(smallerID, resultedOrder[0].getID(), resultedOrderInputsFlipped[0].getID());
            assertEquals(largerID, resultedOrder[1].getID(), resultedOrderInputsFlipped[1].getID());
        }

        // The following are tests pecific to Voter:

        @Test
        void canGetLastVotedRound() {
            voter.getLastVotedRound();
        }

        @Test
        void canSetLastVotedRound() {
            voter.setLastVotedRound(1);
        }

        @ParameterizedTest
        @ValueSource(ints = { -10, -1, 0, 1, 2, 3, 4, 5, 10, 20, 137, 12345 })
        void getLastVotedRoundReturnsValueToWhichItWasSet(int value) {
            voter.setLastVotedRound(value);
            int actual = voter.getLastVotedRound();
            assertEquals(value, actual);
        }

    }

    @Nested
    class DefaultFieldValues {

        @BeforeEach
        void initWithTheCorrectConstructor(RepetitionInfo repetitionInfo) {
            int repetition = repetitionInfo.getCurrentRepetition();
            if (repetition == 1) {
                voter = new Voter();
            } else if (repetition == 2) {
                voter = new Voter("Jane Doe", "12345678", "1995-05-03", true);
            }
        }

        // The following are tests specific to Candidate:

        @RepeatedTest(2)
        void getLastVotedRoundInitiallyReturns0() {
            assertEquals(0, voter.getLastVotedRound());
        }
    }
}
