package metthis.voting_system.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VoterTests {
    private Voter voter;

    @BeforeEach
    void construct() {
        this.voter = new Voter("Jane Roe");
    }

    @Test
    void getName() {
        assertEquals("Jane Roe", this.voter.getName());
    }
}
