package aquaraga.metrics.model;

public class FourKeyMetrics {

    private Deployments deployments;

    public FourKeyMetrics(Deployments deployments) {

        this.deployments = deployments;
    }

    public long deploymentFrequency() {

        return deployments.uniqueCountByCommit();
    }
}
