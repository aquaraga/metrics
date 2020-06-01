package aquaraga.metrics.model;

import java.util.Date;

public class Commit {
    private String sha;
    private Date commitedDate;
    private String[] parentIds;

    public Commit(String commitSha, Date commitedDate, String[] parentIds) {
        this.sha = commitSha;
        this.commitedDate = commitedDate;
        this.parentIds = parentIds;
    }

    public String getSha() {
        return sha;
    }

    public Date getCommitedDate() {
        return commitedDate;
    }

    public boolean isMerge() {
        return parentIds != null && parentIds.length == 2;
    }
}
