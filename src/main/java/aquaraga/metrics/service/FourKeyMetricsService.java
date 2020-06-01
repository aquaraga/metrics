package aquaraga.metrics.service;

import aquaraga.metrics.client.CIClient;
import aquaraga.metrics.model.Commits;
import aquaraga.metrics.model.Deployments;
import aquaraga.metrics.model.FourKeyMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FourKeyMetricsService {

    private CIClient ciClient;

    @Autowired
    public FourKeyMetricsService(CIClient ciClient) {
        this.ciClient = ciClient;
    }

    public FourKeyMetrics metrics() {
        Deployments deployments = ciClient.fetchDeployments();
        Commits commits = ciClient.fetchCommits();
        FourKeyMetrics fourKeyMetrics = new FourKeyMetrics(deployments, commits);
        return fourKeyMetrics;
    }
}
