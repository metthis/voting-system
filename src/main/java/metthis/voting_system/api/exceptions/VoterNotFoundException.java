package metthis.voting_system.api.exceptions;

public class VoterNotFoundException extends NotFoundException {
    public VoterNotFoundException(String id) {
        super("Could not find voter " + id);
    }
}
