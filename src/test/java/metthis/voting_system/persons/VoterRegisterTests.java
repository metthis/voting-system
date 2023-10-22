package metthis.voting_system.persons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class VoterRegisterTests {
    private VoterRegister voterRegister;

    @BeforeEach
    void initEach() {
        this.voterRegister = new VoterRegister();
    }

    @Test
    void registerStartsEmpty() {
        assertTrue(this.voterRegister.howManyRegistered() == 0);
    }

    @Test
    void addIfAbsentAddsAbsentVoter() {
        Voter absentVoter = new Voter("1", "1", "2000-01-01", true);

        this.voterRegister.addIfAbsent(absentVoter);

        assertTrue(this.voterRegister.howManyRegistered() == 1);
        Voter addedVoter = this.voterRegister.getRegister().values().iterator().next();
        assertSame(absentVoter, addedVoter);
    }

    @Test
    void addIfAbsentReturnsNullWhenAddingAbsentVoter() {
        Voter absentVoter = new Voter("1", "1", "2000-01-01", true);

        Voter returned = this.voterRegister.addIfAbsent(absentVoter);

        assertNull(returned);
    }

    @Test
    void addIfAbsentDoesntAddNorUpdateAlreadyRegisteredVoter() {
        Voter voter = new Voter("1", "1", "2000-01-01", true);
        this.voterRegister.addIfAbsent(voter);

        this.voterRegister.addIfAbsent(voter);

        assertTrue(this.voterRegister.howManyRegistered() == 1);
    }

    @Test
    void addIfAbsentDoesntAddNorUpdateAlreadyRegisteredVoterEvenWhenAttributesOtherThanIDChanged() {
        Voter firstVersion = new Voter("original name", "ID", "2000-01-01", true);
        Voter secondVersion = new Voter("new name", "ID", "2020-12-12", false);
        this.voterRegister.addIfAbsent(firstVersion);

        this.voterRegister.addIfAbsent(secondVersion);

        assertTrue(this.voterRegister.howManyRegistered() == 1);
        Voter registeredVoter = this.voterRegister.getRegister().values().iterator().next();
        assertSame(registeredVoter, firstVersion);
    }

    @Test
    void addIfAbsentReturnsAlreadyRegisteredVoterWhenTryingToAddAlreadyRegisteredVoter() {
        Voter voter = new Voter("1", "1", "2000-01-01", true);
        this.voterRegister.addIfAbsent(voter);

        Voter returned = this.voterRegister.addIfAbsent(voter);

        assertSame(returned, voter);
    }

    @Test
    void updateUpdatesAlreadyRegisteredVoter() {
        Voter firstVersion = new Voter("original name", "ID", "2000-01-01", true);
        Voter secondVersion = new Voter("new name", "ID", "2000-01-01", true);
        this.voterRegister.addIfAbsent(firstVersion);

        this.voterRegister.update(secondVersion);

        assertTrue(this.voterRegister.howManyRegistered() == 1);
        Voter registeredVoter = this.voterRegister.getRegister().values().iterator().next();
        assertSame(registeredVoter, secondVersion);
    }

    @Test
    void updateReturnsPreviousVersionWhenUpdatingAlreadyRegisteredVoter() {
        Voter firstVersion = new Voter("original name", "ID", "2000-01-01", true);
        Voter secondVersion = new Voter("new name", "ID", "2000-01-01", true);
        this.voterRegister.addIfAbsent(firstVersion);

        Voter returned = this.voterRegister.update(secondVersion);

        assertSame(returned, firstVersion);
    }

    @Test
    void updateDoesntAddOrUpdateAbsentVoter() {
        Voter first = new Voter("1", "1", "2000-01-01", true);
        Voter second = new Voter("2", "2", "2000-01-01", true);
        this.voterRegister.addIfAbsent(first);

        this.voterRegister.update(second);

        assertTrue(this.voterRegister.howManyRegistered() == 1);
        Voter registeredVoter = this.voterRegister.getRegister().values().iterator().next();
        assertSame(registeredVoter, first);
    }

    @Test
    void updateReturnsNullWhenTryingToAddOrUpdateAbsentVoter() {
        Voter first = new Voter("1", "1", "2000-01-01", true);
        Voter second = new Voter("2", "2", "2000-01-01", true);
        this.voterRegister.addIfAbsent(first);

        Voter returned = this.voterRegister.update(second);

        assertNull(returned);
    }

    @Test
    void addOrUpdateHasSameEffectAndSameReturnAsAddIfAbsentWhenPassedAbsentVoter() {
        Voter absentVoter = new Voter("1", "1", "2000-01-01", true);

        Voter firstReturned = this.voterRegister.addIfAbsent(absentVoter);

        int firstSize = this.voterRegister.howManyRegistered();
        Voter firstAddedVoter = this.voterRegister.getRegister().values().iterator().next();

        this.voterRegister.clear();

        Voter secondReturned = this.voterRegister.addOrUpdate(absentVoter);

        int secondSize = this.voterRegister.howManyRegistered();
        Voter secondAddedVoter = this.voterRegister.getRegister().values().iterator().next();

        assertSame(firstReturned, secondReturned);
        assertEquals(firstSize, secondSize);
        assertSame(firstAddedVoter, secondAddedVoter);
    }

    @Test
    void addOrUpdateHasSameEffectAndSameReturnAsUpdateWhenPassedAlreadyRegisteredVoter() {
        Voter voterFirstVersion = new Voter("original name", "ID", "2000-01-01", true);
        Voter voterSecondVersion = new Voter("new name", "ID", "2000-01-01", true);

        this.voterRegister.addIfAbsent(voterFirstVersion);

        Voter firstReturned = this.voterRegister.update(voterSecondVersion);

        int firstSize = this.voterRegister.howManyRegistered();
        Voter firstUpdatedVoter = this.voterRegister.getRegister().values().iterator().next();

        this.voterRegister.clear();
        this.voterRegister.addIfAbsent(voterFirstVersion);

        Voter secondReturned = this.voterRegister.addOrUpdate(voterSecondVersion);

        int secondSize = this.voterRegister.howManyRegistered();
        Voter secondUpdatedVoter = this.voterRegister.getRegister().values().iterator().next();

        assertSame(firstReturned, secondReturned);
        assertEquals(firstSize, secondSize);
        assertSame(firstUpdatedVoter, secondUpdatedVoter);
    }

    @Test
    void removeRemovesRegisteredVoter() {
        Voter voter = new Voter("1", "1", "2000-01-01", true);
        this.voterRegister.addIfAbsent(voter);

        this.voterRegister.remove(voter);

        assertTrue(this.voterRegister.howManyRegistered() == 0);
    }

    @Test
    void removeReturnsRemovedVoterWhenRemovingRegisteredVoter() {
        Voter voter = new Voter("1", "1", "2000-01-01", true);
        this.voterRegister.addIfAbsent(voter);

        Voter returned = this.voterRegister.remove(voter);

        assertSame(returned, voter);
    }

    @Test
    void removeDoesntRemoveAnyoneWhenTryingToRemoveAnAbsentVoter() {
        Voter firstVoter = new Voter("1", "1", "2000-01-01", true);
        Voter secondVoter = new Voter("2", "2", "2020-12-12", true);
        this.voterRegister.addIfAbsent(firstVoter);

        this.voterRegister.remove(secondVoter);

        assertTrue(this.voterRegister.howManyRegistered() == 1);
        Voter registeredVoter = this.voterRegister.getRegister().values().iterator().next();
        assertSame(registeredVoter, firstVoter);
    }

    @Test
    void removeReturnsNullWhenTryingToRemoveAnAbsentVoter() {
        Voter firstVoter = new Voter("1", "1", "2000-01-01", true);
        Voter secondVoter = new Voter("2", "2", "2020-12-12", true);
        this.voterRegister.addIfAbsent(firstVoter);

        Voter returned = this.voterRegister.remove(secondVoter);

        assertNull(returned);
    }

    @Test
    void clearEmptiesRegister() {
        Voter firstVoter = new Voter("1", "1", "2000-01-01", true);
        Voter secondVoter = new Voter("2", "2", "2020-12-12", true);
        Voter thirdVoter = new Voter("3", "3", "2022-12-12", true);
        this.voterRegister.addIfAbsent(firstVoter);
        this.voterRegister.addIfAbsent(secondVoter);
        this.voterRegister.addIfAbsent(thirdVoter);

        this.voterRegister.clear();

        assertTrue(this.voterRegister.howManyRegistered() == 0);
    }

    @Test
    void containsReturnsTrueWhenBothVotersAreTheSameObject() {
        Voter voter = new Voter("1", "1", "2000-01-01", true);
        this.voterRegister.addIfAbsent(voter);

        boolean actual = this.voterRegister.contains(voter);

        assertTrue(actual);
    }

    @Test
    void containsReturnsFalseWhenVotersHaveTheSameStateButAreDifferentObjects() {
        Voter containedVoter = new Voter("1", "1", "2000-01-01", true);
        Voter checkedVoter = new Voter("1", "1", "2000-01-01", true);
        this.voterRegister.addIfAbsent(containedVoter);

        boolean actual = this.voterRegister.contains(checkedVoter);

        assertFalse(actual);
    }

    @Test
    void containsReturnsFalseWhenVotersOnlyShareID() {
        Voter containedVoter = new Voter("1", "ID", "2000-01-01", true);
        Voter checkedVoter = new Voter("2", "ID", "2020-12-31", false);
        this.voterRegister.addIfAbsent(containedVoter);

        boolean actual = this.voterRegister.contains(checkedVoter);

        assertFalse(actual);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 4, 10, 25, 200, 3000 })
    void getRegisterReturnsRegisterOfTheSameSizeAsInThePrivateVariable(int size) {
        for (int i = 0; i < size; i++) {
            Voter voter = new Voter("name", String.valueOf(i), "2000-01-01", true);
            this.voterRegister.addIfAbsent(voter);
        }
        int internalSize = this.voterRegister.howManyRegistered();

        Map<String, Voter> register = this.voterRegister.getRegister();
        int returnedSize = register.size();

        assertEquals(size, internalSize, returnedSize);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 4, 10, 25, 200, 3000 })
    void howManyRegisteredReturnsCorrectNumberAfterAdditions(int size) {
        for (int i = 0; i < size; i++) {
            Voter voter = new Voter("name", String.valueOf(i), "2000-01-01", true);
            this.voterRegister.addIfAbsent(voter);
        }

        int returned = this.voterRegister.howManyRegistered();

        assertEquals(size, returned);
    }

    // The following are tests of methods specific to VoterRegister:

    @Test
    void howManyVotedReturnsCorrectNumber() {
        int EXPECTED_NUMBER_OF_ACTIVE_VOTERS = 2;

        Voter firstVoter = new Voter("1", "1", "2000-01-01", true);
        Voter secondVoter = new Voter("2", "2", "2020-12-12", true);
        Voter thirdVoter = new Voter("3", "3", "2022-12-12", true);
        this.voterRegister.addIfAbsent(firstVoter);
        this.voterRegister.addIfAbsent(secondVoter);
        this.voterRegister.addIfAbsent(thirdVoter);
        firstVoter.voted();
        secondVoter.voted();

        int activeVoters = this.voterRegister.howManyVoted();

        assertEquals(EXPECTED_NUMBER_OF_ACTIVE_VOTERS, activeVoters);
    }
}
