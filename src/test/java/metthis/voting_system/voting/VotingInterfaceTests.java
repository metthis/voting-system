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
    private BallotBox ballotBox;
    private Voter defaultVoter;

    @BeforeEach
    void initBallotBoxAndVoter() {
        this.ballotBox = new BallotBox();
        this.defaultVoter = new Voter(
                "name", "ID", "1960-01-01", true);
    }

    @Nested
    class New {
        @Test
        void isInstantiatedWithNew() {
            new VotingInterface(ballotBox, defaultVoter);
        }
    }

    @Nested
    class WhenNew {
        private VotingInterface votingInterface;
        private Election election;
        private Vote vote;

        @BeforeEach
        void initVotingInterfaceElectionAndVote() {
            this.votingInterface = new VotingInterface(ballotBox, defaultVoter);
            this.election = new PresidentialElection("2020-01-01");

            Candidate candidate = new Candidate("name", "ID", "1960-01-01", true, "2020-01-01");
            this.vote = new SingleCandidateSingleChoiceVote(candidate);
        }

        @Nested
        class EligibleVoter {
            @BeforeEach
            void makeAndCheckVoterEligible() {
                election.getVoters().addOrUpdate(defaultVoter);
                assertTrue(defaultVoter.isEligible(election), "Voter isn't eligible");
            }

            @Nested
            class HasNotYetVoted {
                @BeforeEach
                void checkHasNotYetVoted() {
                    assertFalse(defaultVoter.getVoted(), "Voter already voted");
                }

                @Test
                void submitVoteReturnsTrueWhenVoterEligibleAndHasntVotedYet() {
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
                void fullyEligibleVotersVotedStateGetsSetToTrue() {
                    votingInterface.submitVote(vote, election);
                    assertTrue(defaultVoter.getVoted());
                }
            }

            @Nested
            class AlreadyVoted {
                @BeforeEach
                void makeAndCheckAlreadyVoted() {
                    defaultVoter.voted();
                    assertTrue(defaultVoter.getVoted());
                }

                @Test
                void submitVoteReturnsFalseWhenVoterAlreadyVoted() {
                    boolean actual = votingInterface.submitVote(vote, election);
                    assertFalse(actual);
                }

                @Test
                void votersVoteIsntAddedToBallotBoxWhenVoterAlreadyVoted() {
                    votingInterface.submitVote(vote, election);
                    assertEquals(0, ballotBox.getVotes().size());
                }

                @Test
                void votersVotedStateDoesntChangeWhenVoterAlreadyVoted() {
                    boolean before = defaultVoter.getVoted();
                    assertTrue(before);

                    votingInterface.submitVote(vote, election);

                    boolean after = defaultVoter.getVoted();
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

                assertFalse(this.ineligibleVoter.isEligible(election));
                assertFalse(this.ineligibleVoter.getVoted());

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
            void ineligibleVotersVoteDoesNotChangeTheirVotedStateWhenNotVotedYet() {
                boolean before = this.ineligibleVoter.getVoted();
                assertFalse(before);

                this.votingInterface.submitVote(vote, election);

                boolean after = this.ineligibleVoter.getVoted();
                assertEquals(before, after);
            }

            @Test
            void ineligibleVotersVoteDoesNotChangeTheirVotedStateWhenAlreadyVoted() {
                this.ineligibleVoter.voted();
                boolean before = this.ineligibleVoter.getVoted();
                assertTrue(before);

                this.votingInterface.submitVote(vote, election);

                boolean after = this.ineligibleVoter.getVoted();
                assertEquals(before, after);
            }
        }
    }
}
