package aquaraga.metrics.model;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class LeadTimeMetrics {
    private List<DeployedCommit> deployedCommits;

    public LeadTimeMetrics(List<DeployedCommit> deployedCommits) {
        this.deployedCommits = deployedCommits;
    }

    public Duration mean() {
        return this.deployedCommits.stream()
                .map(DeployedCommit::getLeadTime)
                .reduce(Duration.ZERO, Duration::plus)
                .dividedBy(deployedCommits.size());
    }

    public LeadTimeOddities oddities(Duration threshold) {
        return new LeadTimeOddities(this.deployedCommits.stream()
                .filter(d -> d.getLeadTime().compareTo(threshold) > 0)
                .map(LeadTimeOddity::new)
                .collect(Collectors.toList()));
    }
}
