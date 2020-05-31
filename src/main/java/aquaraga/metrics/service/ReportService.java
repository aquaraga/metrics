package aquaraga.metrics.service;

import aquaraga.metrics.model.FourKeyMetrics;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
    public void consoleOut(FourKeyMetrics fourKeyMetrics) {
        System.out.printf("Deployment frequency: %d\n", fourKeyMetrics.deploymentFrequency());
    }
}
