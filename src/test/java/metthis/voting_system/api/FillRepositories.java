package metthis.voting_system.api;

import metthis.voting_system.persons.Candidate;
import metthis.voting_system.persons.CandidateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class FillRepositories {

    private static final Logger log = LoggerFactory.getLogger(FillRepositories.class);

    private static final Candidate[] candidates = {
            new Candidate("Chris Wool", "140",
                    "1985-03-15", true, "2023-11-15"),
            new Candidate("Mary Given", "abc789",
                          "1991-09-09", false, "2023-11-30"),
            new Candidate("Elizabeth Wong", "anID",
                          "2001-11-30", true, "2023-12-05")
    };

    @Bean
    CommandLineRunner fillCandidateRepository(CandidateRepository candidateRepository) {
        candidates[1].setWithdrawalDate("2023-12-02");
        candidates[2].setLostThisElection(true);

        return args -> {
            for (Candidate candidate : candidates) {
                log.info("Preloading " + candidateRepository.save(candidate));
            }
        };
    }
}
