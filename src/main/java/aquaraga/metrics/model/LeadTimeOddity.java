package aquaraga.metrics.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class LeadTimeOddity {
    private DeployedCommit deployedCommit;
    private static final String DATE_FORMAT = "MMM d, yyyy HH:mm a";

    public LeadTimeOddity(DeployedCommit deployedCommit) {
        this.deployedCommit = deployedCommit;
    }

    @Override
    public String toString() {
        DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        return String.format("Commit %s made on %s was deployed on %s", deployedCommit.getCommitSha(),
                formatter.format(deployedCommit.getCommitedTime()),
                formatter.format(deployedCommit.getDeploymentTime()));
    }
}
