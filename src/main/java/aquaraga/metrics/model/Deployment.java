package aquaraga.metrics.model;

import java.util.Date;

public class Deployment {
    private final String status;
    private final String commitSha;
    private Date timeDeployed;

    public Deployment(String status, String commitSha, Date timeDeployed) {

        this.status = status;
        this.commitSha = commitSha;
        this.timeDeployed = timeDeployed;
    }

    public String getCommitSha() {
        return commitSha;
    }

    public String getStatus() {
        return status;
    }

    public Date timeDeployed() {
        return timeDeployed;
    }
}
