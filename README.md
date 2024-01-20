## Hi!

This Java project is meant to simulate elections. It currently supports the Czech presidential election which is based on an absolute majority voting system in a single electoral district that spans the whole country.

I'm turning the project into a Spring Boot app with an API. It's WIP but some parts are already done, even if not yet RESTful (more on this below under [Next steps](#next-steps)).

## Package structure

The project consists of 7 packages in `src/main/java`:

1. `persons` defines `Voter` and `Candidate` entities as well as their respective repositories with some custom methods.
2. `voting` defines classes which represent votes (along with a repository), ballot boxes, voting interfaces and voting rounds. `BallotBox`, `VotingInterface` and `VotingRound` will be removed once the project is fully turned into a Spring Boot app because it won't use them anymore.
3. `api` defines controllers, exceptions and an exception handler that allow a user to manipulate entities defined in `persons` and in `voting`. More controllers will be added.
4. `logic` defines the `VotingRoundEvaluator` which analyses  submitted votes and translates them into a result.
5. `elections` contains:
   - `ElectionType`, `ElectionInfo` and `ElectionInfoRepository` which are meant to hold information about an election such as its type, its date and the current voting round;
   - `ElectionRules`, `AbstractElectionRules` and `PresidentialElectionRules` which define rules specific to an election such as candidate and voter eligibility criteria and vote validity criteria;
   - `Election`, `AbstractElection` and `PresidentialElection` which combine the two bullet points above into a single set of classes and will be removed once the project is fully turned into a Spring Boot app.
6. `programs` currently contains `RunPresidentialElection` which mocks votes and produces a winner. It also contains `Run` which just calls `RunPresidentialElection`.
7. `db` contains classes meant to store voters, candidates and votes in a database using raw SQL statements. It'll be removed once the project is fully turned into a Spring Boot app because it'll be replaced by Spring Data.

There is also `VotingSystemApplication`, a class on the same level as all the packages. It launches the Spring Boot app.

Tests can be found in `src/test/java`.

## Entry point

The program can be run by running `metthis.voting_system.programs.Run.main`. Currently, this method runs the presidential election.

The number of candidates and voters can be adjusted in `RunPresidentialElection.registerCandidates` and `.registerVoters`.

The amount of votes for individual candidates can be adjusted in `.runFirstVotingRound` and `.runSecondVotingRound` of the same class.

## Spring Boot app

The Spring Boot app can be launched by running `metthis.voting_system.VotingSystemApplication.main`.

The app currently works with the `persons` and `voting` packages; it allows a user to get and manipulate the contents of `CandidateRepository`, `VoterRepository` and `VoteRepository`.

## Next steps

I'm planning to do the following:

- [WIP] Turn the project into a Spring Boot app with an API
  - Add an `ElectionController` to allow the user to control the election and advance its stages
  - Make the controllers return `RepresentationModel` (instead of `ResponseEntity`) and include links to possible valid actions in the responses
  - Add security features to limit the types of requests different types of users can send
  - Make the repository tests independent of a concrete database implementation (currently the test properties expect a MySQL database with specific settings)
- Encrypt submitted votes to better anonymise them
- Add more kinds of elections