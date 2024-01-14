package metthis.voting_system.api.exceptions;

public class VoteNotFoundException extends NotFoundException {
    public VoteNotFoundException(String id) {
        super("Could not find vote " + id);
    }
}
