package aquaraga.metrics.model;

import java.time.Duration;
import java.util.Date;

public class DeployedCommit {
    private final Commit commit;
    private final Date deploymentTime;

    public DeployedCommit(Commit commit, Date deploymentTime) {

        this.commit = commit;
        this.deploymentTime = deploymentTime;
    }

    public Duration getLeadTime() {
        return Duration.between(commit.getCommitedDate().toInstant(), deploymentTime.toInstant());
    }
}