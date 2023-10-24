package metthis.voting_system.programs;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import metthis.voting_system.elections.Election;
import metthis.voting_system.elections.PresidentialElection;
import metthis.voting_system.logic.VotingRoundEvaluator;
import metthis.voting_system.persons.Candidate;
import metthis.voting_system.persons.CandidateRegister;
import metthis.voting_system.persons.Voter;
import metthis.voting_system.persons.VoterRegister;
import metthis.voting_system.voting.BallotBox;
import metthis.voting_system.voting.SingleCandidateSingleChoiceVote;
import metthis.voting_system.voting.Vote;
import metthis.voting_system.voting.VotingInterface;
import metthis.voting_system.voting.VotingRound;

public class RunPresidentialElection {
    public static void main(String[] args) {
        Election election = new PresidentialElection("2023-10-22");
        registerCandidates(election);
        registerVoters(election);
        VotingRound round1 = runFirstVotingRound(election);
        VotingRoundEvaluator round1Evaluator = new VotingRoundEvaluator(round1, election);
        Candidate[][] winnersFirstRound = round1Evaluator.getRoundWinners(2);
        Candidate winner;

        if (VotingRoundEvaluator.getNumberOfWinners(winnersFirstRound) == 1) {
            winner = winnersFirstRound[0][0];
        } else {
            List<Candidate> winnersFirstRoundSimpleArray = new ArrayList<>();
            for (int i = 0; i < winnersFirstRound.length; i++) {
                winnersFirstRoundSimpleArray.addAll(Arrays.asList(winnersFirstRound[i]));
            }

            election.getCandidates().getRegister().values().stream()
                    .filter(candidate -> !winnersFirstRoundSimpleArray.contains(candidate))
                    .forEach(candidate -> candidate.setLostThisElection(true));
            VotingRound round2 = runSecondVotingRound(election);
            VotingRoundEvaluator round2Evaluator = new VotingRoundEvaluator(round2, election);
            Candidate[][] winnersSecondRound = round2Evaluator.getRoundWinners(1);

            if (VotingRoundEvaluator.getNumberOfWinners(winnersSecondRound) == 1) {
                winner = winnersSecondRound[0][0];
            } else {
                winner = null;
            }
        }

        if (winner == null) {
            System.out.println("There is no winner. A new election is needed.");
        } else {
            System.out.println("Winner: " + winner.getName());
        }
    }

    private static void registerCandidates(Election election) {
        CandidateRegister candidates = election.getCandidates();
        LocalDate dateOfBirth = LocalDate.parse("1960-01-01");

        for (int i = 0; i < 5; i++) {
            Candidate candidate = new Candidate("Candidate" + i, "ID" + i, dateOfBirth.plusDays(i).toString(), true,
                    "2023-10-01");
            candidates.addIfAbsent(candidate);
        }
    }

    private static void registerVoters(Election election) {
        VoterRegister voters = election.getVoters();
        LocalDate dateOfBirth = LocalDate.parse("1960-01-01");

        for (int i = 0; i < 100; i++) {
            Voter voter = new Voter("Voter" + i, "ID" + i, dateOfBirth.plusDays(i).toString(), true);
            voters.addIfAbsent(voter);
        }
    }

    private static VotingRound runFirstVotingRound(Election election) {
        VotingRound round = election.newVotingRound();
        BallotBox ballotBox = round.createBallotBox();
        Candidate[] candidatesArray = election.getCandidates()
                .getRegister().values().toArray(Candidate[]::new);
        Voter[] votersArray = election.getVoters()
                .getRegister().values().toArray(Voter[]::new);
        Arrays.sort(candidatesArray);
        Arrays.sort(votersArray);

        // Each for loop makes some voters vote for one particular candidate.
        for (int i = 0; i < 20; i++) { // 20 votes
            Vote vote = new SingleCandidateSingleChoiceVote(candidatesArray[0]);
            new VotingInterface(ballotBox, votersArray[i]).submitVote(vote, election);
        }

        for (int i = 20; i < 30; i++) { // 10 votes
            Vote vote = new SingleCandidateSingleChoiceVote(candidatesArray[1]);
            new VotingInterface(ballotBox, votersArray[i]).submitVote(vote, election);
        }

        for (int i = 30; i < 70; i++) { // 40 votes
            Vote vote = new SingleCandidateSingleChoiceVote(candidatesArray[2]);
            new VotingInterface(ballotBox, votersArray[i]).submitVote(vote, election);
        }

        for (int i = 70; i < 95; i++) { // 25 votes
            Vote vote = new SingleCandidateSingleChoiceVote(candidatesArray[3]);
            new VotingInterface(ballotBox, votersArray[i]).submitVote(vote, election);
        }

        for (int i = 95; i < 100; i++) { // 5 votes
            Vote vote = new SingleCandidateSingleChoiceVote(candidatesArray[4]);
            new VotingInterface(ballotBox, votersArray[i]).submitVote(vote, election);
        }

        return round;
    }

    private static VotingRound runSecondVotingRound(Election election) {
        VotingRound round = election.newVotingRound();
        BallotBox ballotBox = round.createBallotBox();
        Candidate[] candidatesArray = election.getCandidates().getRegister().values().stream()
                .filter(candidate -> election.isEligibleCandidate(candidate))
                .toArray(Candidate[]::new);
        Voter[] votersArray = election.getVoters().getRegister().values().toArray(Voter[]::new);
        Arrays.sort(candidatesArray);
        Arrays.sort(votersArray);

        // Each for loop makes some voters vote for one particular candidate, the last
        // cast an empty vote.
        for (int i = 0; i < 45; i++) { // 45 votes
            Vote vote = new SingleCandidateSingleChoiceVote(candidatesArray[0]);
            new VotingInterface(ballotBox, votersArray[i]).submitVote(vote, election);
        }

        for (int i = 45; i < 80; i++) { // 35 votes
            Vote vote = new SingleCandidateSingleChoiceVote(candidatesArray[1]);
            new VotingInterface(ballotBox, votersArray[i]).submitVote(vote, election);
        }

        for (int i = 80; i < 100; i++) { // 20 votes
            Vote vote = new SingleCandidateSingleChoiceVote();
            new VotingInterface(ballotBox, votersArray[i]).submitVote(vote, election);
        }

        return round;
    }
}
