package metthis.voting_system.elections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import metthis.voting_system.persons.Candidate;
import metthis.voting_system.persons.Voter;
import metthis.voting_system.voting.SingleCandidateSingleChoiceVote;
import metthis.voting_system.voting.Vote;
import metthis.voting_system.voting.VotingRound;

public class PresidentialElectionTests {
    private String ELECTION_DATE;

    @BeforeEach
    void initELECTION_DATE() {
        this.ELECTION_DATE = "2000-01-01";
    }

    @Nested
    class New {
        @Test
        void isInstantiatedWithStringConstructor() {
            new PresidentialElection(ELECTION_DATE);
        }
    }

    @Nested
    class WhenNew {
        private PresidentialElection election;

        @BeforeEach
        void initPresidentialElection() {
            election = new PresidentialElection(ELECTION_DATE);
        }

        @Nested
        class VoterAndCandidateRegisters {
            @Test
            void canGetVoters() {
                election.getVoters();
            }

            @Test
            void getVotersReturnsEmptyRegister() {
                assertTrue(election.getVoters().getRegister().isEmpty());
            }

            @Test
            void canGetCandidates() {
                election.getCandidates();
            }

            @Test
            void getCandidatesReturnsEmptyRegister() {
                assertTrue(election.getCandidates().getRegister().isEmpty());
            }
        }

        @Nested
        class IsEligibleVoter {
            @ParameterizedTest
            @ValueSource(ints = { 18, 19, 25, 40, 70, 150, 2000 })
            void registeredCitizen18PlusIsEligible(int age) {
                String dateOfBirth = LocalDate.parse(ELECTION_DATE).minusYears(age).toString();
                Voter voter = new Voter("name", "ID", dateOfBirth, true);
                election.getVoters().addIfAbsent(voter);

                assertEquals(age, voter.getAge(LocalDate.parse(ELECTION_DATE)));
                assertTrue(election.isEligibleVoter(voter));
            }

            @Test
            void unregisteredCitizen18IsNotEligible() {
                Voter voter = new Voter("name", "ID", "1982-01-01", true);

                assertEquals(18, voter.getAge(LocalDate.parse(ELECTION_DATE)));
                assertFalse(election.isEligibleVoter(voter));
            }

            @Test
            void registeredNonCitizen18IsNotEligible() {
                Voter voter = new Voter("name", "ID", "1982-01-01", false);
                election.getVoters().addIfAbsent(voter);

                assertEquals(18, voter.getAge(LocalDate.parse(ELECTION_DATE)));
                assertFalse(election.isEligibleVoter(voter));
            }

            @ParameterizedTest
            @ValueSource(ints = { 17, 16, 10, 5, 1, 0, -1, -100, -2000 })
            void registeredCitizen17MinusIsNotEligible(int age) {
                String dateOfBirth = LocalDate.parse(ELECTION_DATE).minusYears(age).toString();
                Voter voter = new Voter("name", "ID", dateOfBirth, true);
                election.getVoters().addIfAbsent(voter);

                assertEquals(age, voter.getAge(LocalDate.parse(ELECTION_DATE)));
                assertFalse(election.isEligibleVoter(voter));
            }
        }

        @Nested
        class IsEligibleCandidate {
            @ParameterizedTest
            @ValueSource(ints = { 40, 41, 50, 75, 90, 150, 2000 })
            void registeredCitizen40PlusNotWithdrawnNotLostIsEligible(int age) {
                String dateOfBirth = LocalDate.parse(ELECTION_DATE).minusYears(age).toString();
                Candidate candidate = new Candidate("name", "ID", dateOfBirth, true, ELECTION_DATE);
                election.getCandidates().addIfAbsent(candidate);

                assertEquals(age, candidate.getAge(LocalDate.parse(ELECTION_DATE)));
                assertNull(candidate.getWithdrawalDate());
                assertFalse(candidate.getLostThisElection());

                assertTrue(election.isEligibleCandidate(candidate));
            }

            @Test
            void unregisteredCitizen40NotWithdrawnNotLostIsNotEligible() {
                Candidate candidate = new Candidate("name", "ID", "1960-01-01", true, ELECTION_DATE);

                assertEquals(40, candidate.getAge(LocalDate.parse(ELECTION_DATE)));
                assertNull(candidate.getWithdrawalDate());
                assertFalse(candidate.getLostThisElection());

                assertFalse(election.isEligibleCandidate(candidate));
            }

            @Test
            void registeredNonCitizen40NotWithdrawnNotLostIsNotEligible() {
                Candidate candidate = new Candidate("name", "ID", "1960-01-01", false, ELECTION_DATE);
                election.getCandidates().addIfAbsent(candidate);

                assertEquals(40, candidate.getAge(LocalDate.parse(ELECTION_DATE)));
                assertNull(candidate.getWithdrawalDate());
                assertFalse(candidate.getLostThisElection());

                assertFalse(election.isEligibleCandidate(candidate));
            }

            @ParameterizedTest
            @ValueSource(ints = { 39, 38, 30, 25, 15, 1, 0, -1, -100, -2000 })
            void registeredCitizen39MinusNotWithdrawnNotLostIsNotEligible(int age) {
                String dateOfBirth = LocalDate.parse(ELECTION_DATE).minusYears(age).toString();
                Candidate candidate = new Candidate("name", "ID", dateOfBirth, true, ELECTION_DATE);
                election.getCandidates().addIfAbsent(candidate);

                assertEquals(age, candidate.getAge(LocalDate.parse(ELECTION_DATE)));
                assertNull(candidate.getWithdrawalDate());
                assertFalse(candidate.getLostThisElection());

                assertFalse(election.isEligibleCandidate(candidate));
            }

            @Test
            void registeredCitizen40HasWithdrawnNotLostIsNotEligible() {
                Candidate candidate = new Candidate("name", "ID", "1960-01-01", true, ELECTION_DATE);
                election.getCandidates().addIfAbsent(candidate);
                candidate.setWithdrawalDate(ELECTION_DATE);

                assertEquals(40, candidate.getAge(LocalDate.parse(ELECTION_DATE)));
                assertFalse(candidate.getLostThisElection());

                assertFalse(election.isEligibleCandidate(candidate));
            }

            @Test
            void registeredCitizen40NotWithdrawnHasLostIsNotEligible() {
                Candidate candidate = new Candidate("name", "ID", "1960-01-01", true, ELECTION_DATE);
                election.getCandidates().addIfAbsent(candidate);
                candidate.setLostThisElection(true);

                assertEquals(40, candidate.getAge(LocalDate.parse(ELECTION_DATE)));
                assertNull(candidate.getWithdrawalDate());

                assertFalse(election.isEligibleCandidate(candidate));
            }
        }

        @Nested
        class VotingRounds {
            @Test
            void canGetVotingRounds() {
                election.getVotingRounds();
            }

            @Test
            void getVotingRoundInitiallyReturnsEmptyCollection() {
                assertEquals(0, election.getVotingRounds().size());
            }

            @Test
            void canNewVotingRound() {
                election.newVotingRound();
            }

            @Test
            void newVotingRoundIncreasesSizeOfGetVotingRoundsTo1() {
                election.newVotingRound();
                assertEquals(1, election.getVotingRounds().size());
            }

            @Test
            void newVotingRoundTimes100IncreasesSizeOfGetVotingRoundsTo100() {
                for (int i = 0; i < 100; i++) {
                    election.newVotingRound();
                }

                assertEquals(100, election.getVotingRounds().size());
            }

            @ParameterizedTest
            @ValueSource(ints = { 0, 1, 2, 5, 10, 25, 100 })
            void newVotingRoundReturnsLastItemFromGetVotingRounds(int roundsAlreadyPresent) {
                for (int i = 0; i < roundsAlreadyPresent; i++) {
                    election.newVotingRound();
                }

                List<VotingRound> allRounds = election.getVotingRounds();
                VotingRound newRound = election.newVotingRound();

                assertSame(allRounds.get(allRounds.size() - 1), newRound);
            }
        }

        @Nested
        class VoteIsValid {
            @Test
            void voteIsValidWhenCandidateEligible() {
                Candidate candidate = new Candidate("name", "ID", "1960-01-01", true, ELECTION_DATE);
                election.getCandidates().addIfAbsent(candidate);
                Vote vote = new SingleCandidateSingleChoiceVote(candidate);

                assertTrue(election.isEligibleCandidate(vote.getChoice()));

                assertTrue(election.voteIsValid(vote));
            }

            @Test
            void voteIsNotValidWhenCandidateNotEligible() {
                Candidate candidate = new Candidate("name", "ID", ELECTION_DATE, false, ELECTION_DATE);
                Vote vote = new SingleCandidateSingleChoiceVote(candidate);

                assertFalse(election.isEligibleCandidate(vote.getChoice()));

                assertFalse(election.voteIsValid(vote));
            }

            @Test
            void voteIsNotValidWhenCandidateNull() {
                Vote vote = new SingleCandidateSingleChoiceVote();

                assertNull(vote.getChoice());

                assertFalse(election.voteIsValid(vote));
            }
        }
    }
}
