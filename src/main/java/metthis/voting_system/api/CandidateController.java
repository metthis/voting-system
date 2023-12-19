package metthis.voting_system.api;

import metthis.voting_system.api.exceptions.CandidateNotFoundException;
import metthis.voting_system.persons.Candidate;
import metthis.voting_system.persons.CandidateRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
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

    @PutMapping("/{id}")
    private ResponseEntity<?> putOne(@PathVariable String id,
                                     @RequestBody Candidate suppliedCandidate,
                                     UriComponentsBuilder ucb) {
        Candidate savedCandidate = candidateRepository.save(suppliedCandidate);

        URI locationOfNewCandidate = ucb
                .path("candidates/{id}")
                .buildAndExpand(savedCandidate.getId())
                .toUri();

        return ResponseEntity.created(locationOfNewCandidate).body(savedCandidate);
    }
}
