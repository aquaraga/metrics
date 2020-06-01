package aquaraga.metrics.model;

import java.util.Date;

public class Commit {
    private String sha;
    private Date commitedDate;

    public Commit(String commitSha, Date commitedDate) {
        this.sha = commitSha;
        this.commitedDate = commitedDate;
    }

    public String getSha() {
        return sha;
    }

    public Date getCommitedDate() {
        return commitedDate;
    }
}
