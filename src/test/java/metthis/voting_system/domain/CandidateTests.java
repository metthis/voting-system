package metthis.voting_system.domain;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class CandidateTests {
    private Candidate candidate;

    @BeforeEach
    void initEach() {
        this.candidate = new Candidate("Jane Doe", "12345678", "1995-11-27", true, "2023-03-15");
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
        LocalDate expected = LocalDate.of(1995, 11, 27);
        assertEquals(expected, this.candidate.getDateOfBirth());
    }

    @Test
    void canGetIsCitizen() {
        assertEquals(true, this.candidate.getIsCitizen());
    }

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
}
