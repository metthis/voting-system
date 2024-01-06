package metthis.voting_system.api;

import jakarta.validation.Valid;
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
    private ResponseEntity<Candidate> getOne(@PathVariable String id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new CandidateNotFoundException(id));
        return ResponseEntity.ok(candidate);
    }

    @GetMapping
    private ResponseEntity<List<Candidate>> getAll() {
        List<Candidate> candidates = candidateRepository.findAll();
        return ResponseEntity.ok(candidates);
    }

    @PutMapping("/{id}")
    private ResponseEntity<?> putOne(@PathVariable String id,
                                     @Valid @RequestBody Candidate suppliedCandidate,
                                     UriComponentsBuilder ucb) {
        Candidate candidateToSave = getCandidateWithIdIfMissing(suppliedCandidate, id);
        boolean candidateAlreadyExisted = candidateRepository.existsById(id);
        Candidate savedCandidate = candidateRepository.save(candidateToSave);

        if (candidateAlreadyExisted) {
            return ResponseEntity.noContent().build();
        }

        URI locationOfNewCandidate = ucb
                .path("candidates/{id}")
                .buildAndExpand(savedCandidate.getId())
                .toUri();

        return ResponseEntity.created(locationOfNewCandidate).body(savedCandidate);
    }

    private static Candidate getCandidateWithIdIfMissing(Candidate suppliedCandidate, String id) {
        if (suppliedCandidate.getId() != null) {
            return suppliedCandidate;
        }

        Candidate candidateToSave = new Candidate(suppliedCandidate.getName(),
                                        id,
                                        suppliedCandidate.getDateOfBirth().toString(),
                                        suppliedCandidate.getIsCitizen(),
                                        suppliedCandidate.getRegistrationDate().toString());

        candidateToSave.setWithdrawalDate(suppliedCandidate.getWithdrawalDate().toString());
        candidateToSave.setLostThisElection(suppliedCandidate.getLostThisElection());

        return candidateToSave;
    }

    // Supports updates of withdrawalDate and lostThisElection.
    // Doesn't support setting these fields to null (in such a case the field in question isn't updated).
    // Other fields are ignored.
    // Fields which don't correspond to Candidate fields are also ignored.
    @PatchMapping("/{id}")
    private ResponseEntity<?> patchOne(@PathVariable String id,
                                       @RequestBody Candidate updateData) {
        Candidate candidate = candidateRepository.findById(id).get();

        if (updateData.getWithdrawalDate() != null) {
            candidate.setWithdrawalDate(updateData.getWithdrawalDate().toString());
        }
        if (updateData.getLostThisElection() != null) {
            candidate.setLostThisElection(updateData.getLostThisElection());
        }

        Candidate savedCandidate = candidateRepository.save(candidate);
        return ResponseEntity.ok(savedCandidate);
    }
}
