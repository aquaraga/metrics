package aquaraga.metrics.client;

import aquaraga.metrics.model.Commits;
import aquaraga.metrics.model.Deployments;

import org.springframework.stereotype.Service;

@Service
public interface CIClient {
    Deployments fetchDeployments();
    Commits fetchCommits();
}
