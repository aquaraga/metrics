package aquaraga.metrics.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class FourKeyMetrics {

    private Deployments deployments;
    private Commits commits;

    public FourKeyMetrics(Deployments deployments, Commits commits) {

        this.deployments = deployments;
        this.commits = commits;
    }

    public long deploymentFrequency() {

        return deployments.uniqueCountByCommit();
    }

    public LeadTimeMetrics leadTime() {
        Deployment latestDeployment = deployments.latestSuccessful();
        if (latestDeployment == null) {
            return new LeadTimeMetrics(Collections.emptyList());
        }
        List<Commit> commitsWithDeployments = this.commits.commitsUntil(latestDeployment);
        Date deploymentTime = latestDeployment.timeDeployed();
        List<DeployedCommit> deployedCommits = new ArrayList<>();
        for (Commit commit :
                commitsWithDeployments) {
            Deployment deployment = deployments.forCommit(commit);
            if (deployment != null) {
                deploymentTime = deployment.timeDeployed();
            }
            deployedCommits.add(new DeployedCommit(commit, deploymentTime));
        }
        return new LeadTimeMetrics(deployedCommits.stream()
                .map(dc -> dc.getLeadTime()).collect(Collectors.toList()));
    }
}
