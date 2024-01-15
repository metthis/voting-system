package metthis.voting_system.api;

import jakarta.validation.Valid;
import metthis.voting_system.api.exceptions.CandidateNotFoundException;
import metthis.voting_system.api.exceptions.VoteNotFoundException;
import metthis.voting_system.persons.CandidateRepository;
import metthis.voting_system.voting.Vote;
import metthis.voting_system.voting.VoteRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/votes")
public class VoteController {

    private final CandidateRepository candidateRepository;

    private final VoteRepository voteRepository;

    public VoteController(CandidateRepository candidateRepository, VoteRepository voteRepository) {
        this.candidateRepository = candidateRepository;
        this.voteRepository = voteRepository;
    }

    @GetMapping("/{id}")
    private ResponseEntity<Vote> getOne(@PathVariable String id) {
        Vote vote = voteRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new VoteNotFoundException(id));
        return ResponseEntity.ok(vote);
    }

    @GetMapping
    private ResponseEntity<List<Vote>> getAll() {
        Sort sort = Sort.by("votingRound", "choice").ascending();
        List<Vote> votes = voteRepository.findAll(sort);
        return ResponseEntity.ok(votes);
    }

    @PostMapping()
    private ResponseEntity<?> postOne(@Valid @RequestBody Vote suppliedVote,
                                     UriComponentsBuilder ucb) {
        String candidateId = suppliedVote.getChoice().getId();
        candidateRepository.findById(candidateId)
                .orElseThrow(() -> new CandidateNotFoundException(candidateId));

        Vote savedVote = voteRepository.save(suppliedVote);

        URI locationOfNewVote = ucb
                .path("votes/{id}")
                .buildAndExpand(savedVote.getId().toString())
                .toUri();

        return ResponseEntity.created(locationOfNewVote).body(savedVote);
    }

    @DeleteMapping()
    private ResponseEntity<?> deleteAll() {
        voteRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
