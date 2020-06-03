package aquaraga.metrics.service;

import aquaraga.metrics.model.FourKeyMetrics;
import aquaraga.metrics.model.LeadTimeMetrics;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class ReportService {
    public void consoleOut(FourKeyMetrics fourKeyMetrics) {
        LeadTimeMetrics leadTimeMetrics = fourKeyMetrics.leadTime();
        Duration meanLeadTime = leadTimeMetrics.mean();
        System.out.printf("Average Lead Time: %d days %d hours\n", meanLeadTime.toDays(),
                meanLeadTime.toHoursPart());
        System.out.printf("Deployment frequency: %d\n", fourKeyMetrics.deploymentFrequency());
        System.out.printf("Change failure rate: %d%%\n", (int) Math.round(fourKeyMetrics.changeFailureRate() * 100));

        System.out.println("Lead time oddities (commits with lead time >= 10 days):\n" +
                leadTimeMetrics.oddities(Duration.ofDays(10)));
    }
}
