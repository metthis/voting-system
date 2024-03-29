package metthis.voting_system.persons;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CandidateRegisterTests {
    private CandidateRegister candidateRegister;

    @BeforeEach
    void initEach() {
        this.candidateRegister = new CandidateRegister();
    }

    @Test
    void registerStartsEmpty() {
        assertTrue(this.candidateRegister.howManyRegistered() == 0);
    }

    @Test
    void addIfAbsentAddsAbsentCandidate() {
        Candidate absentCandidate = new Candidate("1", "1", "2000-01-01", true, "2023-01-01");

        this.candidateRegister.addIfAbsent(absentCandidate);

        assertTrue(this.candidateRegister.howManyRegistered() == 1);
        Candidate addedCandidate = this.candidateRegister.getRegister().values().iterator().next();
        assertSame(absentCandidate, addedCandidate);
    }

    @Test
    void addIfAbsentReturnsNullWhenAddingAbsentCandidate() {
        Candidate absentCandidate = new Candidate("1", "1", "2000-01-01", true, "2023-01-01");

        Candidate returned = this.candidateRegister.addIfAbsent(absentCandidate);

        assertNull(returned);
    }

    @Test
    void addIfAbsentDoesntAddNorUpdateAlreadyRegisteredCandidate() {
        Candidate candidate = new Candidate("1", "1", "2000-01-01", true, "2023-01-01");
        this.candidateRegister.addIfAbsent(candidate);

        this.candidateRegister.addIfAbsent(candidate);

        assertTrue(this.candidateRegister.howManyRegistered() == 1);
    }

    @Test
    void addIfAbsentDoesntAddNorUpdateAlreadyRegisteredCandidateEvenWhenAttributesOtherThanIDChanged() {
        Candidate firstVersion = new Candidate("original name", "ID", "2000-01-01", true, "2023-01-01");
        Candidate secondVersion = new Candidate("new name", "ID", "2020-12-12", false, "2023-01-01");
        this.candidateRegister.addIfAbsent(firstVersion);

        this.candidateRegister.addIfAbsent(secondVersion);

        assertTrue(this.candidateRegister.howManyRegistered() == 1);
        Candidate registeredCandidate = this.candidateRegister.getRegister().values().iterator().next();
        assertSame(registeredCandidate, firstVersion);
    }

    @Test
    void addIfAbsentReturnsAlreadyRegisteredCandidateWhenTryingToAddAlreadyRegisteredCandidate() {
        Candidate candidate = new Candidate("1", "1", "2000-01-01", true, "2023-01-01");
        this.candidateRegister.addIfAbsent(candidate);

        Candidate returned = this.candidateRegister.addIfAbsent(candidate);

        assertSame(returned, candidate);
    }

    @Test
    void updateUpdatesAlreadyRegisteredCandidate() {
        Candidate firstVersion = new Candidate("original name", "ID", "2000-01-01", true, "2023-01-01");
        Candidate secondVersion = new Candidate("new name", "ID", "2000-01-01", true, "2023-01-01");
        this.candidateRegister.addIfAbsent(firstVersion);

        this.candidateRegister.update(secondVersion);

        assertTrue(this.candidateRegister.howManyRegistered() == 1);
        Candidate registeredCandidate = this.candidateRegister.getRegister().values().iterator().next();
        assertSame(registeredCandidate, secondVersion);
    }

    @Test
    void updateReturnsPreviousVersionWhenUpdatingAlreadyRegisteredCandidate() {
        Candidate firstVersion = new Candidate("original name", "ID", "2000-01-01", true, "2023-01-01");
        Candidate secondVersion = new Candidate("new name", "ID", "2000-01-01", true, "2023-01-01");
        this.candidateRegister.addIfAbsent(firstVersion);

        Candidate returned = this.candidateRegister.update(secondVersion);

        assertSame(returned, firstVersion);
    }

    @Test
    void updateDoesntAddOrUpdateAbsentCandidate() {
        Candidate first = new Candidate("1", "1", "2000-01-01", true, "2023-01-01");
        Candidate second = new Candidate("2", "2", "2000-01-01", true, "2023-01-01");
        this.candidateRegister.addIfAbsent(first);

        this.candidateRegister.update(second);

        assertTrue(this.candidateRegister.howManyRegistered() == 1);
        Candidate registeredCandidate = this.candidateRegister.getRegister().values().iterator().next();
        assertSame(registeredCandidate, first);
    }

    @Test
    void updateReturnsNullWhenTryingToAddOrUpdateAbsentCandidate() {
        Candidate first = new Candidate("1", "1", "2000-01-01", true, "2023-01-01");
        Candidate second = new Candidate("2", "2", "2000-01-01", true, "2023-01-01");
        this.candidateRegister.addIfAbsent(first);

        Candidate returned = this.candidateRegister.update(second);

        assertNull(returned);
    }

    @Test
    void addOrUpdateHasSameEffectAndSameReturnAsAddIfAbsentWhenPassedAbsentCandidate() {
        Candidate absentCandidate = new Candidate("1", "1", "2000-01-01", true, "2023-01-01");

        Candidate firstReturned = this.candidateRegister.addIfAbsent(absentCandidate);

        int firstSize = this.candidateRegister.howManyRegistered();
        Candidate firstAddedCandidate = this.candidateRegister.getRegister().values().iterator().next();

        this.candidateRegister.clear();

        Candidate secondReturned = this.candidateRegister.addOrUpdate(absentCandidate);

        int secondSize = this.candidateRegister.howManyRegistered();
        Candidate secondAddedCandidate = this.candidateRegister.getRegister().values().iterator().next();

        assertSame(firstReturned, secondReturned);
        assertEquals(firstSize, secondSize);
        assertSame(firstAddedCandidate, secondAddedCandidate);
    }

    @Test
    void addOrUpdateHasSameEffectAndSameReturnAsUpdateWhenPassedAlreadyRegisteredCandidate() {
        Candidate candidateFirstVersion = new Candidate("original name", "ID", "2000-01-01", true, "2023-01-01");
        Candidate candidateSecondVersion = new Candidate("new name", "ID", "2000-01-01", true, "2023-01-01");

        this.candidateRegister.addIfAbsent(candidateFirstVersion);

        Candidate firstReturned = this.candidateRegister.update(candidateSecondVersion);

        int firstSize = this.candidateRegister.howManyRegistered();
        Candidate firstUpdatedCandidate = this.candidateRegister.getRegister().values().iterator().next();

        this.candidateRegister.clear();
        this.candidateRegister.addIfAbsent(candidateFirstVersion);

        Candidate secondReturned = this.candidateRegister.addOrUpdate(candidateSecondVersion);

        int secondSize = this.candidateRegister.howManyRegistered();
        Candidate secondUpdatedCandidate = this.candidateRegister.getRegister().values().iterator().next();

        assertSame(firstReturned, secondReturned);
        assertEquals(firstSize, secondSize);
        assertSame(firstUpdatedCandidate, secondUpdatedCandidate);
    }

    @Test
    void removeRemovesRegisteredCandidate() {
        Candidate candidate = new Candidate("1", "1", "2000-01-01", true, "2023-01-01");
        this.candidateRegister.addIfAbsent(candidate);

        this.candidateRegister.remove(candidate);

        assertTrue(this.candidateRegister.howManyRegistered() == 0);
    }

    @Test
    void removeReturnsRemovedCandidateWhenRemovingRegisteredCandidate() {
        Candidate candidate = new Candidate("1", "1", "2000-01-01", true, "2023-01-01");
        this.candidateRegister.addIfAbsent(candidate);

        Candidate returned = this.candidateRegister.remove(candidate);

        assertSame(returned, candidate);
    }

    @Test
    void removeDoesntRemoveAnyoneWhenTryingToRemoveAnAbsentCandidate() {
        Candidate firstCandidate = new Candidate("1", "1", "2000-01-01", true, "2023-01-01");
        Candidate secondCandidate = new Candidate("2", "2", "2020-12-12", true, "2023-01-01");
        this.candidateRegister.addIfAbsent(firstCandidate);

        this.candidateRegister.remove(secondCandidate);

        assertTrue(this.candidateRegister.howManyRegistered() == 1);
        Candidate registeredCandidate = this.candidateRegister.getRegister().values().iterator().next();
        assertSame(registeredCandidate, firstCandidate);
    }

    @Test
    void removeReturnsNullWhenTryingToRemoveAnAbsentCandidate() {
        Candidate firstCandidate = new Candidate("1", "1", "2000-01-01", true, "2023-01-01");
        Candidate secondCandidate = new Candidate("2", "2", "2020-12-12", true, "2023-01-01");
        this.candidateRegister.addIfAbsent(firstCandidate);

        Candidate returned = this.candidateRegister.remove(secondCandidate);

        assertNull(returned);
    }

    @Test
    void clearEmptiesRegister() {
        Candidate firstCandidate = new Candidate("1", "1", "2000-01-01", true, "2023-01-01");
        Candidate secondCandidate = new Candidate("2", "2", "2020-12-12", true, "2023-01-01");
        Candidate thirdCandidate = new Candidate("3", "3", "2022-12-12", true, "2023-01-01");
        this.candidateRegister.addIfAbsent(firstCandidate);
        this.candidateRegister.addIfAbsent(secondCandidate);
        this.candidateRegister.addIfAbsent(thirdCandidate);

        this.candidateRegister.clear();

        assertTrue(this.candidateRegister.howManyRegistered() == 0);
    }

    @Test
    void isEmptyReturnsTrueWhenEmpty() {
        assertTrue(this.candidateRegister.isEmpty());
    }

    @Test
    void isEmptyReturnsFalseWhenOneCandidateWasAdded() {
        Candidate candidate = new Candidate("1", "1", "2000-01-01", true, "2023-01-01");
        this.candidateRegister.addIfAbsent(candidate);
        assertFalse(this.candidateRegister.isEmpty());
    }

    @Test
    void containsReturnsTrueWhenBothCandidatesAreTheSameObject() {
        Candidate candidate = new Candidate("1", "1", "2000-01-01", true, "2000-01-01");
        this.candidateRegister.addIfAbsent(candidate);

        boolean actual = this.candidateRegister.contains(candidate);

        assertTrue(actual);
    }

    @Test
    void containsReturnsFalseWhenCandidatesHaveTheSameStateButAreDifferentObjects() {
        Candidate containedCandidate = new Candidate("1", "1", "2000-01-01", true, "2000-01-01");
        Candidate checkedCandidate = new Candidate("1", "1", "2000-01-01", true, "2000-01-01");
        this.candidateRegister.addIfAbsent(containedCandidate);

        boolean actual = this.candidateRegister.contains(checkedCandidate);

        assertFalse(actual);
    }

    @Test
    void containsReturnsFalseWhenCandidatesOnlyShareID() {
        Candidate containedCandidate = new Candidate("1", "ID", "2000-01-01", true, "2000-01-01");
        Candidate checkedCandidate = new Candidate("2", "ID", "2020-12-31", false, "2020-12-31");
        this.candidateRegister.addIfAbsent(containedCandidate);

        boolean actual = this.candidateRegister.contains(checkedCandidate);

        assertFalse(actual);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 4, 10, 25, 200, 3000 })
    void getRegisterReturnsRegisterOfTheSameSizeAsInThePrivateVariable(int size) {
        for (int i = 0; i < size; i++) {
            Candidate candidate = new Candidate("name", String.valueOf(i), "2000-01-01", true, "2023-01-01");
            this.candidateRegister.addIfAbsent(candidate);
        }
        int internalSize = this.candidateRegister.howManyRegistered();

        Map<String, Candidate> register = this.candidateRegister.getRegister();
        int returnedSize = register.size();

        assertEquals(size, internalSize, returnedSize);
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 2, 4, 10, 25, 200, 3000 })
    void howManyRegisteredReturnsCorrectNumberAfterAdditions(int size) {
        for (int i = 0; i < size; i++) {
            Candidate candidate = new Candidate("name", String.valueOf(i), "2000-01-01", true, "2023-01-01");
            this.candidateRegister.addIfAbsent(candidate);
        }

        int returned = this.candidateRegister.howManyRegistered();

        assertEquals(size, returned);
    }

    // The following are tests of methods specific to CandidateRegister:

    @Test
    void howManyWithdrewReturnsCorrectNumber() {
        int EXPECTED_NUMBER_OF_WITHDRAWN_CANDIDATES = 2;

        Candidate firstCandidate = new Candidate("1", "1", "2000-01-01", true, "2023-01-01");
        Candidate secondCandidate = new Candidate("2", "2", "2020-12-12", true, "2023-01-01");
        Candidate thirdCandidate = new Candidate("3", "3", "2022-12-12", true, "2023-01-01");
        this.candidateRegister.addIfAbsent(firstCandidate);
        this.candidateRegister.addIfAbsent(secondCandidate);
        this.candidateRegister.addIfAbsent(thirdCandidate);
        firstCandidate.setWithdrawalDate("2023-02-01");
        secondCandidate.setWithdrawalDate("2023-02-01");

        int withdrawnCandidates = this.candidateRegister.howManyWithdrew();

        assertEquals(EXPECTED_NUMBER_OF_WITHDRAWN_CANDIDATES, withdrawnCandidates);
    }
}
