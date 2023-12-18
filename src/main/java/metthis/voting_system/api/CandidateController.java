package metthis.voting_system.api;

import metthis.voting_system.api.exceptions.CandidateNotFoundException;
import metthis.voting_system.persons.Candidate;
import metthis.voting_system.persons.CandidateRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/candidates")
public class CandidateController {

    private final CandidateRepository candidateRepository;

    public CandidateController(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    @GetMapping("/{id}")
    private ResponseEntity<Candidate> one(@PathVariable String id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new CandidateNotFoundException(id));
        return ResponseEntity.ok(candidate);
    }

    @GetMapping
    private ResponseEntity<List<Candidate>> all() {
        List<Candidate> candidates = candidateRepository.findAll();
        return ResponseEntity.ok(candidates);
    }
}
