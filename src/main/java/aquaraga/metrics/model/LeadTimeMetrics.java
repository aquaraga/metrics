package aquaraga.metrics.model;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class LeadTimeMetrics {
    private final List<Duration> leadTimes;

    public LeadTimeMetrics(List<Duration> leadTimes) {
        this.leadTimes = leadTimes;
    }

    public Duration mean() {
        System.out.println("All durations " + leadTimes);
        return this.leadTimes.stream()
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus)
                .dividedBy(leadTimes.size());
    }
}
