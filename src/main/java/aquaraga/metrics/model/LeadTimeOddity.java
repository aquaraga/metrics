package aquaraga.metrics.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;

public class LeadTimeOddity {
    private DeployedCommit deployedCommit;
    private static final String DATE_FORMAT = "MMM d, yyyy HH:mm a";

    public LeadTimeOddity(DeployedCommit deployedCommit) {
        this.deployedCommit = deployedCommit;
    }

    public Duration leadTime() {
        return deployedCommit.getLeadTime();
    }

    @Override
    public String toString() {
        DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        Duration leadTime = leadTime();
        return String.format("[%d days %d hours] Commit %s made on %s was deployed on %s",
                leadTime.toDays(),
                leadTime.toHoursPart(),
                deployedCommit.getCommitSha(),
                formatter.format(deployedCommit.getCommitedTime()),
                formatter.format(deployedCommit.getDeploymentTime()));
    }
}
