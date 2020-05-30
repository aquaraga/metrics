package aquaraga.metrics.client.dto;

public class Deployment {
    private String status;
    private String updated_at;
    private Deployable deployable;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Deployable getDeployable() {
        return deployable;
    }

    public void setDeployable(Deployable deployable) {
        this.deployable = deployable;
    }
}
