package aquaraga.metrics.service;

import aquaraga.metrics.client.CIClient;
import aquaraga.metrics.config.CIConfiguration;
import aquaraga.metrics.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FourKeyMetricsService {

    private CIClient ciClient;
    private CIConfiguration configuration;

    @Autowired
    public FourKeyMetricsService(CIClient ciClient, CIConfiguration configuration) {
        this.ciClient = ciClient;
        this.configuration = configuration;
    }

    public FourKeyMetrics metrics() {
        return metrics(new DurationWindow(configuration.getDeploymentWindowInDays(),
                configuration.getShiftLeftInDays()));
    }

    public FourKeyMetrics metrics(DurationWindow durationWindow) {
        Deployments deployments = ciClient.fetchDeployments(durationWindow);
        Commits commits = ciClient.fetchCommits(durationWindow);
        if (deployments.successCount() == 0) {
            return new FourKeyMetrics(deployments, commits);
        }

        Deployment earliestDeployment = deployments.earliestSuccessful();
        Deployment latestDeploymentOutsideWindow = ciClient.successfulDeploymentsPreceding(earliestDeployment)
                .latestSuccessful();
        Commits commitsOutsideWindow = ciClient.fetchCommitsBetween(latestDeploymentOutsideWindow.getCommitSha(),
                earliestDeployment.getCommitSha());

        Commits allCommits = commits.concat(commitsOutsideWindow);
        FourKeyMetrics fourKeyMetrics = new FourKeyMetrics(deployments, allCommits);
        return fourKeyMetrics;
    }
}
