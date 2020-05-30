package aquaraga.metrics.model;

public class Deployment {
    private final String status;
    private final String commitSha;

    public Deployment(String status, String commitSha) {

        this.status = status;
        this.commitSha = commitSha;
    }

    public String getCommitSha() {
        return commitSha;
    }

    public String getStatus() {
        return status;
    }
}
