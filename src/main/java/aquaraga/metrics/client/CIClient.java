package aquaraga.metrics.client;

import aquaraga.metrics.model.Deployments;
import org.springframework.stereotype.Service;

@Service
public interface CIClient {
    Deployments fetchDeployments();
}
