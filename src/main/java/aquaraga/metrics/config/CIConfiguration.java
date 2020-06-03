package aquaraga.metrics.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties
public class CIConfiguration {

    private String apiToken;
    private String projectAPIUrl;
    private String prodEnvironmentName;
    private int deploymentWindowInDays;
    private int shiftLeftInDays;

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getProjectAPIUrl() {
        return projectAPIUrl;
    }

    public void setProjectAPIUrl(String projectAPIUrl) {
        this.projectAPIUrl = projectAPIUrl;
    }

    public String getProdEnvironmentName() {
        return prodEnvironmentName;
    }

    public void setProdEnvironmentName(String prodEnvironmentName) {
        this.prodEnvironmentName = prodEnvironmentName;
    }

    public int getDeploymentWindowInDays() {
        return deploymentWindowInDays;
    }

    public void setDeploymentWindowInDays(int deploymentWindowInDays) {
        this.deploymentWindowInDays = deploymentWindowInDays;
    }

    public int getShiftLeftInDays() {
        return shiftLeftInDays;
    }

    public void setShiftLeftInDays(int shiftLeftInDays) {
        this.shiftLeftInDays = shiftLeftInDays;
    }
}
