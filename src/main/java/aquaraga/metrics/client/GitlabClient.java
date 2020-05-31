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
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

        int pageNumber = 1;
        List<Deployment> deploymentsForPage;
        List<Deployment> allDeployments = new ArrayList<>();
        do {
            deploymentsForPage = getDeploymentsForPage(pageNumber);
            allDeployments.addAll(deploymentsForPage);
            pageNumber++;
        } while(deploymentsForPage.size() == 100);

        return new Deployments(allDeployments.stream()
                .filter(d -> d.getStatus().equalsIgnoreCase("success")
                        || d.getStatus().equalsIgnoreCase("failed"))
                .map(d -> new aquaraga.metrics.model.Deployment(d.getStatus(),
                        d.getDeployable().getCommit().getId()))
                .collect(Collectors.toList()));
    }

    private List<Deployment> getDeploymentsForPage(int pageNumber) {
        String deploymentsUrl = String.format("%s/deployments?per_page=100&page=%d&environment=%s&sort=desc&order_by=created_at&updated_after=%s",
                ciConfiguration.getProjectAPIUrl(),
                pageNumber,
                ciConfiguration.getProdEnvironmentName(),
                getDeploymentThresholdTime());

        var client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(deploymentsUrl))
                .header("PRIVATE-TOKEN", ciConfiguration.getApiToken())
                .GET()
                .build();
        Deployment[] deploymentsFromGitlab;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            deploymentsFromGitlab = new Gson().fromJson(response.body(), Deployment[].class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error when fetching deployments from Gitlab", e);
        }
        return Arrays.asList(deploymentsFromGitlab);
    }

    private String getDeploymentThresholdTime() {
        return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmX")
                .withZone(ZoneOffset.UTC).format
                        (Instant.now().minus(ciConfiguration.getRunHistoryInDays(), ChronoUnit.DAYS));
    }
}
