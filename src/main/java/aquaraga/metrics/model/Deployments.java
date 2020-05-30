package aquaraga.metrics.model;

import java.util.List;

public class Deployments {
    private List<Deployment> deployments;

    public Deployments(List<Deployment> deployments) {

        this.deployments = deployments;
    }

    public long uniqueCountByCommit() {
        return deployments.stream().map(d -> d.getCommitSha()).distinct().count();
    }
}
