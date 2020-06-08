package aquaraga.metrics.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Commits {
    private List<Commit> commits;

    public Commits(List<Commit> commits) {
        this.commits = commits;
    }

    public List<Commit> commitsUntil(Deployment latest) {
        if (latest == null) {
            return Collections.emptyList();
        }
        return commits.stream()
                    .dropWhile(c -> !c.getSha().equalsIgnoreCase(latest.getCommitSha()))
                    .collect(Collectors.toList());
    }

    public Commits concat(Commits commits) {
        ArrayList<Commit> underlyingCommits = new ArrayList<>(this.commits);
        underlyingCommits.addAll(commits.commits);
        return new Commits(underlyingCommits);
    }
}
