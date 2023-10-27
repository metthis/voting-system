## Hi!

This Java project is meant to simulate elections. It currently supports the Czech presidential election which is based on an absolute majority voting system in a single electoral district that spans the whole country.

## Package structure

The project consists of 5 packages in `src/main/java`:

- `persons` defines `Voter` and `Candidate` classes as well as `VoterRegister` and `CandidateRegister` which hold voters and candidates
- `voting` defines classes which represent votes, ballot boxes, voting interfaces and voting rounds
- `logic` defines the `VotingRoundEvaluator` which analyzes  submitted votes and translates them into a result
- `elections` are meant to hold classes containing rules which are specific to different kinds of elections, such as voter and candidate eligibility criteria and vote validity criteria; currently, the only such implemented class is `PresidentialElection`
- `programs` currently contains only `RunPresidentialElection` which mocks votes and produces a winner

Unit tests for `persons`, `voting`, `logic` and `elections` can be found in `src/test/java`.

## Entry point

The program can be run by running `metthis.voting_system.programs.Run`. Currently, this method runs the presidential election.

The number of candidates and voters can be adjusted in `RunPresidentialElection.registerCandidates` and `.registerVoters`.

The amount of votes for individual candidates can be adjusted in `.runFirstVotingRound` and `.runSecondVotingRound` of the same class.

## Next steps

I'm still planning to do the following:

- Store voters, candidates and votes in a database rather than in runtime memory
- Make the `BallotBox` class encrypt votes submitted into it better anonymise the votes
- Add more kinds of elections