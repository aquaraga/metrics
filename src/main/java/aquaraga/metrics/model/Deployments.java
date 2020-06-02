package aquaraga.metrics.model;

import java.util.*;
import java.util.stream.Collectors;

public class Deployments {
    private final List<Deployment> deployments;

    public Deployments(List<Deployment> deployments) {
        this.deployments = deployments;
    }

    private Map<String, List<Deployment>> deploymentsByCommitSha() {
        return deployments.stream().collect(Collectors.groupingBy(Deployment::getCommitSha));
    }

    public long uniqueCountByCommit() {
        return deploymentsByCommitSha().size();
    }

    public Deployment latestSuccessful() {
        return latestSuccessful(deployments);
    }

    private Deployment latestSuccessful(List<Deployment> deployments) {
        return deployments.stream()
                .filter(x -> x.getStatus().equalsIgnoreCase("success"))
                .max(Comparator.comparing(Deployment::timeDeployed))
                .orElse(null);
    }

    public Deployment latestSuccessfulForCommit(Commit commit) {

        List<Deployment> deployments = deploymentsByCommitSha().get(commit.getSha());
        if (deployments == null) {
            return null;
        }
        return latestSuccessful(deployments);
    }

    public long failureCount() {
        return countByStatus("failed");
    }

    public long successCount() {
        return countByStatus("success");
    }

    private long countByStatus(String status) {
        return this.deployments.stream().filter(x -> x.getStatus().equalsIgnoreCase(status)).count();
    }
}
