package aquaraga.metrics.client;

import aquaraga.metrics.client.dto.Deployment;
import aquaraga.metrics.config.CIConfiguration;
import aquaraga.metrics.model.Deployments;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class GitlabClient implements CIClient {

    private CIConfiguration ciConfiguration;

    @Autowired
    public GitlabClient(CIConfiguration ciConfiguration) {
        this.ciConfiguration = ciConfiguration;
    }

    @Override
    public Deployments fetchDeployments() {

        String deploymentsUrl = String.format("%s/deployments?per_page=100&page=1&environment=%s&sort=desc&order_by=created_at",
                ciConfiguration.getProjectAPIUrl(),
                ciConfiguration.getProdEnvironmentName());

        var client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(deploymentsUrl))
                .header("PRIVATE-TOKEN", ciConfiguration.getApiToken())
                .GET()
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Deployment[] deploymentsFromGitlab = new Gson().fromJson(response.body(), Deployment[].class);
            return new Deployments(Arrays.stream(deploymentsFromGitlab)
                    .filter(d -> d.getStatus().equalsIgnoreCase("success")
                            || d.getStatus().equalsIgnoreCase("failed"))
                    .map(d -> new aquaraga.metrics.model.Deployment(d.getStatus(),
                            d.getDeployable().getCommit().getId()))
                    .collect(Collectors.toList()));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error when fetching deployments from Gitlab", e);
        }
    }
}
