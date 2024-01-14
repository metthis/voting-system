package metthis.voting_system.api.exceptions;

public class CandidateNotFoundException extends NotFoundException {
    public CandidateNotFoundException(String id) {
        super("Could not find candidate " + id);
    }
}
