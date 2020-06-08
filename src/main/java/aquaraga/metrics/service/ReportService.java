package aquaraga.metrics.service;

import aquaraga.metrics.model.DurationWindow;
import aquaraga.metrics.model.FourKeyMetrics;
import aquaraga.metrics.model.LeadTimeMetrics;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

@Service
public class ReportService {
    public void consoleOut(FourKeyMetrics fourKeyMetrics) {
        LeadTimeMetrics leadTimeMetrics = fourKeyMetrics.leadTime();
        if (leadTimeMetrics == null) {
            System.out.println("No deployment data found for the duration");
            return;
        }
        Duration meanLeadTime = leadTimeMetrics.mean();
        if (meanLeadTime == null) {
            System.out.println("No deployment data found for the duration");
            return;
        }
        System.out.printf("Average Lead Time: %d days %d hours\n", meanLeadTime.toDays(),
                meanLeadTime.toHoursPart());
        System.out.printf("Deployment frequency: %d\n", fourKeyMetrics.deploymentFrequency());
        System.out.printf("Change failure rate: %d%%\n", (int) Math.round(fourKeyMetrics.changeFailureRate() * 100));

        System.out.println("Lead time oddities (commits with lead time >= 10 days):\n" +
                leadTimeMetrics.oddities(Duration.ofDays(10)));
    }

    public void consoleOutTrend(DurationWindow duration, FourKeyMetrics fourKeyMetrics) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                .withLocale(Locale.US)
                .withZone(ZoneId.systemDefault());
        LeadTimeMetrics leadTimeMetrics = fourKeyMetrics.leadTime();
        if (leadTimeMetrics == null) {
            System.out.printf("%s - %s,-,0,-\n",
                    dateTimeFormatter.format(duration.beginning()),
                    dateTimeFormatter.format(duration.end()));
            return;
        }
        Duration meanLeadTime = leadTimeMetrics.mean();
        if (meanLeadTime == null) {
            System.out.printf("%s - %s,-,0,-\n",
                    dateTimeFormatter.format(duration.beginning()),
                    dateTimeFormatter.format(duration.end()));
            return;
        }

        System.out.printf("%s - %s,%d days %d hours,%d,%d%%\n",
                dateTimeFormatter.format(duration.beginning()),
                dateTimeFormatter.format(duration.end()),
                meanLeadTime.toDays(),
                meanLeadTime.toHoursPart(), fourKeyMetrics.deploymentFrequency(),
                (int) Math.round(fourKeyMetrics.changeFailureRate() * 100));

    }
}
