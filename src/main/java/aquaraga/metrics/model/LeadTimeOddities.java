package aquaraga.metrics.model;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LeadTimeOddities {
    private List<LeadTimeOddity> oddities;

    public LeadTimeOddities(List<LeadTimeOddity> oddities) {
        this.oddities = oddities;
    }

    @Override
    public String toString() {
        return oddities.stream()
                .sorted(Comparator.comparing(LeadTimeOddity::leadTime).reversed())
                .map(o -> o.toString())
                .collect(Collectors.joining("\n"));
    }
}
