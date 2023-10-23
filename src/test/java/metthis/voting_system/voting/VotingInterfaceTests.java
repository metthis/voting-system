package metthis.voting_system.voting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import metthis.voting_system.elections.Election;
import metthis.voting_system.elections.PresidentialElection;
import metthis.voting_system.persons.Candidate;
import metthis.voting_system.persons.Voter;

public class VotingInterfaceTests {
    private Voter defaultVoter;

    @BeforeEach
    void initVoter() {
        this.defaultVoter = new Voter(
                "name", "ID", "1960-01-01", true);
    }

    @Nested
    class New {
        private BallotBox ballotBox;

        @BeforeEach
        void initVoter() {
            this.ballotBox = new BallotBox();
        }

        @Test
        void isInstantiatedWithNew() {
            new VotingInterface(ballotBox, defaultVoter);
        }
    }

    @Nested
    class WhenNew {
        private Election election;
        private VotingRound round;
        private BallotBox ballotBox;
        private VotingInterface votingInterface;
        private Vote vote;

        @BeforeEach
        void initVotingInterfaceElectionVotingRoundBallotBoxAndVote() {
            this.election = new PresidentialElection("2020-01-01");
            this.round = election.newVotingRound();
            this.ballotBox = round.createBallotBox();
            this.votingInterface = new VotingInterface(ballotBox, defaultVoter);

            Candidate candidate = new Candidate("name", "ID", "1960-01-01", true, "2020-01-01");
            this.vote = new SingleCandidateSingleChoiceVote(candidate);
        }

        @Nested
        class EligibleVoter {
            @BeforeEach
            void makeAndCheckVoterEligible() {
                election.getVoters().addOrUpdate(defaultVoter);
                assertTrue(election.isEligibleVoter(defaultVoter), "Voter isn't eligible");
            }

            @Nested
            class HasNotYetVotedInRound1 {
                @BeforeEach
                void checkHasNotYetVotedInRound1() {
                    assertEquals(0, defaultVoter.getLastVotedRound(), "Voter already voted");
                }

                @Test
                void submitVoteReturnsTrueWhenVoterEligibleAndHasntVotedYetInRound1() {
                    assertTrue(votingInterface.submitVote(vote, election));
                }

                @Test
                void fullyEligibleVotersVoteGetsAddedToBallotBox() {
                    int before = ballotBox.getVotes().getOrDefault(vote, 0);
                    votingInterface.submitVote(vote, election);
                    int after = ballotBox.getVotes().getOrDefault(vote, 0);
                    assertEquals(before + 1, after);
                }

                @Test
                void fullyEligibleVotersLastVotedRoundGetsSetTo1() {
                    votingInterface.submitVote(vote, election);
                    assertEquals(1, defaultVoter.getLastVotedRound());
                }
            }

            @Nested
            class AlreadyVoted {
                @BeforeEach
                void makeAndCheckAlreadyVotedInRound1() {
                    defaultVoter.setLastVotedRound(1);
                    assertEquals(1, defaultVoter.getLastVotedRound());
                }

                @Test
                void submitVoteReturnsFalseWhenVoterAlreadyVotedInRound1() {
                    boolean actual = votingInterface.submitVote(vote, election);
                    assertFalse(actual);
                }

                @Test
                void votersVoteIsntAddedToBallotBoxWhenVoterAlreadyVotedInRound1() {
                    votingInterface.submitVote(vote, election);
                    assertEquals(0, ballotBox.getVotes().size());
                }

                @Test
                void votersLastVotedRoundDoesntChangeWhenVoterAlreadyVoted() {
                    int before = defaultVoter.getLastVotedRound();
                    assertEquals(1, before);

                    votingInterface.submitVote(vote, election);

                    int after = defaultVoter.getLastVotedRound();
                    assertEquals(before, after);
                }
            }
        }

        @Nested
        class IneligibleVoter {
            private Voter ineligibleVoter;
            private VotingInterface votingInterface;

            @BeforeEach
            void initIneligibleVoterAndTheirVotingInterface() {
                this.ineligibleVoter = new Voter("name", "ID", "2022-01-01", false);

                assertFalse(election.isEligibleVoter(this.ineligibleVoter));
                assertEquals(0, this.ineligibleVoter.getLastVotedRound());

                this.votingInterface = new VotingInterface(ballotBox, ineligibleVoter);
            }

            @Test
            void submitVoteReturnsFalseWhenIneligibleVoter() {
                boolean actual = this.votingInterface.submitVote(vote, election);
                assertFalse(actual);
            }

            @Test
            void ineligibleVotersVoteIsNotAddedToBallotBox() {
                this.votingInterface.submitVote(vote, election);
                assertEquals(0, ballotBox.getVotes().size());
            }

            @Test
            void ineligibleVotersVoteDoesNotChangeTheirLastVotedRoundWhenNotVotedYetInRound1() {
                int before = this.ineligibleVoter.getLastVotedRound();
                assertEquals(0, before);

                this.votingInterface.submitVote(vote, election);

                int after = this.ineligibleVoter.getLastVotedRound();
                assertEquals(before, after);
            }

            @Test
            void ineligibleVotersVoteDoesNotChangeTheirLastVotedRoundWhenAlreadyVotedInRound1() {
                this.ineligibleVoter.setLastVotedRound(1);
                int before = this.ineligibleVoter.getLastVotedRound();
                assertEquals(1, before);

                this.votingInterface.submitVote(vote, election);

                int after = this.ineligibleVoter.getLastVotedRound();
                assertEquals(before, after);
            }
        }
    }
}
