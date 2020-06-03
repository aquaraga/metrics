package aquaraga.metrics.client;

import aquaraga.metrics.model.Commits;
import aquaraga.metrics.model.Deployments;

import aquaraga.metrics.model.DurationWindow;
import org.springframework.stereotype.Service;

@Service
public interface CIClient {

    Deployments fetchDeployments(DurationWindow durationWindow);

    Commits fetchCommits(DurationWindow durationWindow);
}
