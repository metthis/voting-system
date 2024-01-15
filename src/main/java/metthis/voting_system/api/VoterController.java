package metthis.voting_system.api;

import jakarta.validation.Valid;
import metthis.voting_system.api.exceptions.VoterNotFoundException;
import metthis.voting_system.persons.Voter;
import metthis.voting_system.persons.VoterRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/voters")
public class VoterController {

    private final VoterRepository voterRepository;

    public VoterController(VoterRepository voterRepository) {
        this.voterRepository = voterRepository;
    }

    @GetMapping("/{id}")
    private ResponseEntity<Voter> getOne(@PathVariable String id) {
        Voter voter = voterRepository.findById(id)
                .orElseThrow(() -> new VoterNotFoundException(id));
        return ResponseEntity.ok(voter);
    }

    @GetMapping
    private ResponseEntity<List<Voter>> getAll() {
        List<Voter> voters = voterRepository.findAll();
        return ResponseEntity.ok(voters);
    }

    @PutMapping("/{id}")
    private ResponseEntity<?> putOne(@PathVariable String id,
                                     @Valid @RequestBody Voter suppliedVoter,
                                     UriComponentsBuilder ucb) {
        Voter voterToSave = getVoterWithIdIfMissing(suppliedVoter, id);
        boolean voterAlreadyExisted = voterRepository.existsById(id);
        Voter savedVoter = voterRepository.save(voterToSave);

        if (voterAlreadyExisted) {
            return ResponseEntity.noContent().build();
        }

        URI locationOfNewVoter = ucb
                .path("voters/{id}")
                .buildAndExpand(savedVoter.getId())
                .toUri();

        return ResponseEntity.created(locationOfNewVoter).body(savedVoter);
    }

    private static Voter getVoterWithIdIfMissing(Voter suppliedVoter, String id) {
        if (suppliedVoter.getId() != null) {
            return suppliedVoter;
        }

        Voter voterToSave = new Voter(suppliedVoter.getName(),
                                                  id,
                                                  suppliedVoter.getDateOfBirth().toString(),
                                                  suppliedVoter.getIsCitizen());

        voterToSave.setLastVotedRound(suppliedVoter.getLastVotedRound());

        return voterToSave;
    }

    // Supports updates of lastVotedRound.
    // Doesn't support setting this field to null (in such a case the field isn't updated).
    // Other fields are ignored.
    // Fields which don't correspond to Voter fields are also ignored.
    @PatchMapping("/{id}")
    private ResponseEntity<?> patchOne(@PathVariable String id,
                                       @RequestBody Voter updateData) {
        Voter voter = voterRepository.findById(id)
                .orElseThrow(() -> new VoterNotFoundException(id));

        if (updateData.getLastVotedRound() != null) {
            voter.setLastVotedRound(updateData.getLastVotedRound());
        }

        Voter savedVoter = voterRepository.save(voter);
        return ResponseEntity.ok(savedVoter);
    }

    @DeleteMapping()
    private ResponseEntity<?> deleteAll() {
        voterRepository.deleteAll();
        return ResponseEntity.noContent().build();
    }
}
