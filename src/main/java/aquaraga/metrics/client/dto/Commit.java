package aquaraga.metrics.client.dto;

public class Commit {
    private String id;
    private String committed_date;
    private String[] parent_ids;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommitted_date() {
        return committed_date;
    }

    public void setCommitted_date(String committed_date) {
        this.committed_date = committed_date;
    }

    public String[] getParent_ids() {
        return parent_ids;
    }

    public void setParent_ids(String[] parent_ids) {
        this.parent_ids = parent_ids;
    }
}
