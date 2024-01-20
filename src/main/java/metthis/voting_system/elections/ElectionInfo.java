package metthis.voting_system.elections;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
public class ElectionInfo {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(nullable = false)
    private ElectionType TYPE;

    @NotNull
    @Column(nullable = false)
    private LocalDate DATE;

    @NotNull
    @Column(nullable = false)
    private Integer votingRound;

    public ElectionInfo(ElectionType type, String date) {
        this.TYPE = type;
        this.DATE = date == null ? null : LocalDate.parse(date);
        this.votingRound = 0;
    }

    public ElectionInfo() {
    }

    public ElectionType getType() {
        return this.TYPE;
    }

    public LocalDate getDate() {
        return this.DATE;
    }

    public Integer getVotingRound() {
        return votingRound;
    }

    public void setVotingRound(Integer votingRound) {
        this.votingRound = votingRound;
    }
}
