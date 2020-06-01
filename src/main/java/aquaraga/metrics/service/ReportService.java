package aquaraga.metrics.service;

import aquaraga.metrics.model.FourKeyMetrics;
import aquaraga.metrics.model.LeadTimeMetrics;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class ReportService {
    public void consoleOut(FourKeyMetrics fourKeyMetrics) {
        LeadTimeMetrics leadTimeMetrics = fourKeyMetrics.leadTime();
        System.out.printf("Average Lead Time: %s\n", leadTimeMetrics.mean());
        System.out.printf("Deployment frequency: %d\n", fourKeyMetrics.deploymentFrequency());

        System.out.println("Lead time oddities: " + leadTimeMetrics.oddities(Duration.ofDays(10)));
    }
}
