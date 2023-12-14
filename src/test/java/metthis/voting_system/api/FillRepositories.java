package metthis.voting_system.api;

import metthis.voting_system.persons.Candidate;
import metthis.voting_system.persons.CandidateRepository;
import org.assertj.core.util.Arrays;
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
                    "1985-03-15", true, "2023-11-15")
    };

    @Bean
    CommandLineRunner fillCandidateRepository(CandidateRepository candidateRepository) {
        return args -> {
            for (Candidate candidate : candidates) {
                log.info("Preloading " + candidateRepository.save(candidate));
            }
        };
    }
}
