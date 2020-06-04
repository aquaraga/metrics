package aquaraga.metrics.model;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;

public class DurationWindow {
    private final int deploymentWindowInDays;
    private final int shiftLeftInDays;

    public DurationWindow(int deploymentWindowInDays, int shiftLeftInDays) {
        this.deploymentWindowInDays = deploymentWindowInDays;
        this.shiftLeftInDays = shiftLeftInDays;
    }

    public int getDeploymentWindowInDays() {
        return deploymentWindowInDays;
    }

    public int getShiftLeftInDays() {
        return shiftLeftInDays;
    }

    public TemporalAccessor beginning() {
        return (Instant.now()
                        .minus(shiftLeftInDays, ChronoUnit.DAYS)
                        .minus(deploymentWindowInDays, ChronoUnit.DAYS));
    }

    public TemporalAccessor end() {
        return Instant.now().minus(shiftLeftInDays, ChronoUnit.DAYS);
    }

}
