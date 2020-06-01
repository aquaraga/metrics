package aquaraga.metrics.model;

import java.util.*;
import java.util.stream.Collectors;

public class Deployments {
    private final Map<String, List<Deployment>> deploymentsByCommitSha;
    private List<Deployment> deployments;

    public Deployments(List<Deployment> deployments) {

        this.deployments = deployments;
        this.deploymentsByCommitSha =
                deployments.stream().collect(Collectors.groupingBy(Deployment::getCommitSha));
//        deployments.stream().collect(Collectors.groupingBy(Deployment::getCommitSha))
//                .entrySet()
//                .stream()
//                .map(e -> new Object(){
//                    String commitSha = e.getKey();
//                    DeploymentsByCommitSha deploymentsByCommitSha =
//                            new DeploymentsByCommitSha(e.getValue());
//                }).collect(Collectors.toMap(o -> o.commitSha, o -> o.deploymentsByCommitSha));
    }

    public long uniqueCountByCommit() {
        return deploymentsByCommitSha.size();
    }

    public Deployment latestSuccessful() {
        return deploymentsByCommitSha.values().stream()
                .flatMap(List::stream)
                .filter(x -> x.getStatus().equalsIgnoreCase("success"))
                .max(Comparator.comparing(Deployment::timeDeployed))
                .orElse(null);
    }

    public Deployment forCommit(Commit commit) {

        List<Deployment> deployments = deploymentsByCommitSha.get(commit.getSha());
        if (deployments == null) {
            return null;
        }
        return deployments.stream()
                .filter(x -> x.getStatus().equalsIgnoreCase("success"))
                .max(Comparator.comparing(Deployment::timeDeployed))
                .orElse(null);
    }
}
