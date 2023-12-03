package metthis.voting_system.persons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

@SpringBootTest
public class VoterRepositoryTests {
    @Autowired
    private VoterRepository voterRepository;

    @AfterEach
    void deleteAll() {
        voterRepository.deleteAll();
    }

    @Test
    void repositoryStartsEmpty() {
        assertEquals(0, this.voterRepository.count());
    }

    @Test
    void isEmptyReturnsTrueWhenEmpty() {
        assertTrue(this.voterRepository.isEmpty());
    }

    @Test
    void isEmptyReturnsFalseWhenOneVoterWasAdded() {
        Voter voter = new Voter("1", "1", "2000-01-01", true);
        this.voterRepository.save(voter);
        assertFalse(this.voterRepository.isEmpty());
    }

    @Test
    void existsReturnsTrueWhenBothVotersAreTheSameObject() {
        Voter voter = new Voter("1", "1", "2000-01-01", true);
        this.voterRepository.save(voter);

        assertTrue(this.voterRepository.exists(voter));
    }

    @Test
    void existsReturnsTrueWhenVotersHaveTheSameStateButAreDifferentObjects() {
        Voter containedVoter = new Voter("1", "1", "2000-01-01", true);
        Voter checkedVoter = new Voter("1", "1", "2000-01-01", true);
        this.voterRepository.save(containedVoter);

        assertTrue(this.voterRepository.exists(checkedVoter));
    }

    @Test
    void existsReturnsTrueWhenVotersOnlyShareID() {
        Voter containedVoter = new Voter("1", "ID", "2000-01-01", true);
        Voter checkedVoter = new Voter("2", "ID", "2020-12-31", false);
        this.voterRepository.save(containedVoter);

        assertTrue(this.voterRepository.exists(checkedVoter));
    }

    @Test
    void existsReturnsFalseWhenVotersHaveDifferentIDs() {
        Voter containedVoter = new Voter("name", "1", "2000-01-01", true);
        Voter checkedVoter = new Voter("name", "2", "2000-01-01", true);
        this.voterRepository.save(containedVoter);

        assertFalse(this.voterRepository.exists(checkedVoter));
    }

    // The following are tests of methods specific to VoterRepository:

    @Test
    void howManyVotedThrowsExceptionWhenSomeoneAlreadyVotedInFollowingRound() {
        int EXAMINED_VOTING_ROUND = 3;

        Voter firstVoter = new Voter("1", "1", "2000-01-01", true);
        Voter secondVoter = new Voter("2", "2", "2020-12-12", true);

        firstVoter.setLastVotedRound(EXAMINED_VOTING_ROUND);
        secondVoter.setLastVotedRound(EXAMINED_VOTING_ROUND + 1);

        this.voterRepository.save(firstVoter);
        this.voterRepository.save(secondVoter);

        assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            this.voterRepository.howManyVoted(EXAMINED_VOTING_ROUND);
        });

    }

    @Test
    void howManyVotedReturnsCorrectNumberWhenNobodyVotedInFollowingRound() {
        int EXAMINED_VOTING_ROUND = 3;
        int EXPECTED_NUMBER_OF_ACTIVE_VOTERS = 2;

        Voter firstVoter = new Voter("1", "1", "2000-01-01", true);
        Voter secondVoter = new Voter("2", "2", "2020-12-12", true);
        Voter thirdVoter = new Voter("3", "3", "2021-01-01", true);
        Voter fourthVoter = new Voter("4", "4", "2021-12-12", true);

        firstVoter.setLastVotedRound(3);
        secondVoter.setLastVotedRound(3);
        thirdVoter.setLastVotedRound(1);

        this.voterRepository.save(firstVoter);
        this.voterRepository.save(secondVoter);
        this.voterRepository.save(thirdVoter);
        this.voterRepository.save(fourthVoter);

        int activeVoters = this.voterRepository.howManyVoted(EXAMINED_VOTING_ROUND);

        assertEquals(EXPECTED_NUMBER_OF_ACTIVE_VOTERS, activeVoters);
    }
}
