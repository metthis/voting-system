package metthis.voting_system.elections;

import metthis.voting_system.persons.Candidate;
import metthis.voting_system.persons.CandidateRepository;
import metthis.voting_system.persons.Voter;
import metthis.voting_system.persons.VoterRepository;
import metthis.voting_system.voting.SingleCandidateSingleChoiceVote;
import metthis.voting_system.voting.Vote;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/test.properties")
// TestInstance.Lifecycle.PER_CLASS is selected to allow the use of @BeforeAll and @AfterAll on a non-static repository
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PresidentialElectionRulesTests {

    @Autowired
    private VoterRepository voterRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private ElectionInfoRepository electionInfoRepository;

    private ElectionInfo electionInfo;

    @Autowired
    @Qualifier("presidentialElectionRules")
    private ElectionRules electionRules;

    @BeforeAll
    void initAndSaveElectionInfo() {
        electionInfo = new ElectionInfo(ElectionType.PRESIDENTIAL_ELECTION, "2000-01-01");
        electionInfoRepository.save(electionInfo);
    }

    @AfterAll
    void clearElectionInfoRepository() {
        electionInfoRepository.deleteAll();
    }

    @Nested
    // TestInstance.Lifecycle.PER_CLASS is selected to allow the use of @AfterAll on a non-static repository
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class IsEligibleVoter {

        @BeforeEach
        @AfterAll
        void clearRepository() {
            voterRepository.deleteAll();
        }

        @ParameterizedTest
        @ValueSource(ints = { 18, 19, 25, 40, 70, 150, 2000 })
        void registeredCitizen18PlusIsEligible(int age) {
            String dateOfBirth = electionInfo.getDate().minusYears(age).toString();
            Voter voter = new Voter("name", "ID", dateOfBirth, true);
            voterRepository.save(voter);

            assertEquals(age, voter.getAge(electionInfo.getDate()));
            assertTrue(electionRules.isEligibleVoter(voter, electionInfo));
        }

        @Test
        void unregisteredCitizen18IsNotEligible() {
            Voter voter = new Voter("name", "ID", "1982-01-01", true);

            assertEquals(18, voter.getAge(electionInfo.getDate()));
            assertFalse(electionRules.isEligibleVoter(voter, electionInfo));
        }

        @Test
        void registeredNonCitizen18IsNotEligible() {
            Voter voter = new Voter("name", "ID", "1982-01-01", false);
            voterRepository.save(voter);

            assertEquals(18, voter.getAge(electionInfo.getDate()));
            assertFalse(electionRules.isEligibleVoter(voter, electionInfo));
        }

        @ParameterizedTest
        @ValueSource(ints = { 17, 16, 10, 5, 1, 0, -1, -100, -2000 })
        void registeredCitizen17MinusIsNotEligible(int age) {
            String dateOfBirth = electionInfo.getDate().minusYears(age).toString();
            Voter voter = new Voter("name", "ID", dateOfBirth, true);
            voterRepository.save(voter);

            assertEquals(age, voter.getAge(electionInfo.getDate()));
            assertFalse(electionRules.isEligibleVoter(voter, electionInfo));
        }
    }

    @Nested
    // TestInstance.Lifecycle.PER_CLASS is selected to allow the use of @AfterAll on a non-static repository
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class IsEligibleCandidate {

        @BeforeEach
        @AfterAll
        void clearRepository() {
            candidateRepository.deleteAll();
        }

        @ParameterizedTest
        @ValueSource(ints = { 40, 41, 50, 75, 90, 150, 2000 })
        void registeredCitizen40PlusNotWithdrawnNotLostIsEligible(int age) {
            String dateOfBirth = electionInfo.getDate().minusYears(age).toString();
            Candidate candidate = new Candidate("name", "ID", dateOfBirth,
                                                true, electionInfo.getDate().toString());
            candidateRepository.save(candidate);

            assertEquals(age, candidate.getAge(electionInfo.getDate()));
            assertNull(candidate.getWithdrawalDate());
            assertFalse(candidate.getLostThisElection());

            assertTrue(electionRules.isEligibleCandidate(candidate, electionInfo));
        }

        @Test
        void unregisteredCitizen40NotWithdrawnNotLostIsNotEligible() {
            Candidate candidate = new Candidate("name", "ID", "1960-01-01",
                                                true, electionInfo.getDate().toString());

            assertEquals(40, candidate.getAge(electionInfo.getDate()));
            assertNull(candidate.getWithdrawalDate());
            assertFalse(candidate.getLostThisElection());

            assertFalse(electionRules.isEligibleCandidate(candidate, electionInfo));
        }

        @Test
        void registeredNonCitizen40NotWithdrawnNotLostIsNotEligible() {
            Candidate candidate = new Candidate("name", "ID", "1960-01-01",
                                                false, electionInfo.getDate().toString());
            candidateRepository.save(candidate);

            assertEquals(40, candidate.getAge(electionInfo.getDate()));
            assertNull(candidate.getWithdrawalDate());
            assertFalse(candidate.getLostThisElection());

            assertFalse(electionRules.isEligibleCandidate(candidate, electionInfo));
        }

        @ParameterizedTest
        @ValueSource(ints = { 39, 38, 30, 25, 15, 1, 0, -1, -100, -2000 })
        void registeredCitizen39MinusNotWithdrawnNotLostIsNotEligible(int age) {
            String dateOfBirth = electionInfo.getDate().minusYears(age).toString();
            Candidate candidate = new Candidate("name", "ID", dateOfBirth,
                                                true, electionInfo.getDate().toString());
            candidateRepository.save(candidate);

            assertEquals(age, candidate.getAge(electionInfo.getDate()));
            assertNull(candidate.getWithdrawalDate());
            assertFalse(candidate.getLostThisElection());

            assertFalse(electionRules.isEligibleCandidate(candidate, electionInfo));
        }

        @Test
        void registeredCitizen40HasWithdrawnNotLostIsNotEligible() {
            Candidate candidate = new Candidate("name", "ID", "1960-01-01",
                                                true, electionInfo.getDate().toString());
            candidate.setWithdrawalDate(electionInfo.getDate().toString());
            candidateRepository.save(candidate);

            assertEquals(40, candidate.getAge(electionInfo.getDate()));
            assertFalse(candidate.getLostThisElection());

            assertFalse(electionRules.isEligibleCandidate(candidate, electionInfo));
        }

        @Test
        void registeredCitizen40NotWithdrawnHasLostIsNotEligible() {
            Candidate candidate = new Candidate("name", "ID", "1960-01-01",
                                                true, electionInfo.getDate().toString());
            candidate.setLostThisElection(true);
            candidateRepository.save(candidate);

            assertEquals(40, candidate.getAge(electionInfo.getDate()));
            assertNull(candidate.getWithdrawalDate());

            assertFalse(electionRules.isEligibleCandidate(candidate, electionInfo));
        }
    }

    @Nested
    // TestInstance.Lifecycle.PER_CLASS is selected to allow the use of @AfterAll on a non-static repository
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class VoteIsValid {

        @BeforeEach
        @AfterAll
        void clearRepository() {
            candidateRepository.deleteAll();
        }

        @Test
        void voteIsValidWhenCandidateEligible() {
            Candidate candidate = new Candidate("name", "ID", "1960-01-01",
                                                true, electionInfo.getDate().toString());
            candidateRepository.save(candidate);
            Vote vote = new SingleCandidateSingleChoiceVote(candidate);

            assertTrue(electionRules.isEligibleCandidate(vote.getChoice(), electionInfo));

            assertTrue(electionRules.voteIsValid(vote, electionInfo));
        }

        @Test
        void voteIsNotValidWhenCandidateNotEligible() {
            Candidate candidate = new Candidate("name", "ID", electionInfo.getDate().toString(),
                                                false, electionInfo.getDate().toString());
            Vote vote = new SingleCandidateSingleChoiceVote(candidate);

            assertFalse(electionRules.isEligibleCandidate(vote.getChoice(), electionInfo));

            assertFalse(electionRules.voteIsValid(vote, electionInfo));
        }

        @Test
        void voteIsNotValidWhenCandidateNull() {
            Vote vote = new SingleCandidateSingleChoiceVote();

            assertNull(vote.getChoice());

            assertFalse(electionRules.voteIsValid(vote, electionInfo));
        }
    }
}

