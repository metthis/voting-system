package metthis.voting_system.api.exceptions;

public class VoterNotFoundException extends RuntimeException {
    public VoterNotFoundException(String id) {
        super("Could not find voter " + id);
    }
}
