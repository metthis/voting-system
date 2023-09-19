package metthis.voting_system.persons;

public class WinnersInfo {
    private Candidate[] winners;
    private int[] ties;

    /*
     * If some candidates in winning positions are tied by vote count,
     * they must all be included in "candidates".
     * 
     * In such a case, all these candidates must have the same integer
     * in the respective indices of "ties". This integer is the index
     * of the first of these tied candidates.
     * 
     * If a particular candidate isn't tied with anyone, their respective
     * tie value must be negative.
     * 
     * Example:
     * candidates = {A, B, C, D, E, F}
     * A and B are tied. D-F are also tied.
     * "ties" must then be as follows.
     * ties = {0, 0, -1, 3, 3, 3}
     */

    public WinnersInfo(Candidate[] winners, int[] ties) {
        this.verifyEqualLength(winners, ties);
        this.verifyNoCandidateIsNull(winners);
        this.verifyValidTieValues(ties);

        this.winners = winners;
        this.ties = ties;
    }

    private void verifyEqualLength(Candidate[] winners, int[] ties) {
        if (winners.length == ties.length) {
            return;
        }
        String message = new StringBuilder()
                .append("Both arrays must be of same lenght but are ")
                .append(winners.length)
                .append(" and ")
                .append(ties.length)
                .toString();
        throw new IllegalArgumentException(message);
    }

    private void verifyNoCandidateIsNull(Candidate[] winners) {
        for (Candidate winner : winners) {
            if (winner == null) {
                String message = "At least one winner is null";
                throw new IllegalArgumentException(message);
            }
        }
    }

    private void verifyValidTieValues(int[] ties) {
        for (int value : ties) {
            if (value >= ties.length) {
                String message = "At least one tie value is larger than or equal to the number of winners";
                throw new IllegalArgumentException(message);
            }
        }
    }

    public Candidate[] getWinners() {
        return this.winners;
    }

    public int[] getTies() {
        return this.ties;
    }
}
