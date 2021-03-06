package aquaraga.metrics.client;

import aquaraga.metrics.client.dto.Commit;
import aquaraga.metrics.client.dto.Deployment;
import aquaraga.metrics.config.CIConfiguration;
import aquaraga.metrics.model.Commits;
import aquaraga.metrics.model.Deployments;
import aquaraga.metrics.model.DurationWindow;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GitlabClient implements CIClient {

    public static final DateTimeFormatter GITLAB_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmX")
            .withZone(ZoneOffset.UTC);
    private CIConfiguration ciConfiguration;

    @Autowired
    public GitlabClient(CIConfiguration ciConfiguration) {
        this.ciConfiguration = ciConfiguration;
    }

    @Override
    public Deployments fetchDeployments(DurationWindow durationWindow) {

        int pageNumber = 1;
        List<Deployment> deploymentsForPage;
        List<Deployment> allDeployments = new ArrayList<>();
        do {
            deploymentsForPage = getDeploymentsForPage(pageNumber, durationWindow);
            allDeployments.addAll(deploymentsForPage);
            pageNumber++;
        } while(deploymentsForPage.size() == 100);

        return new Deployments(allDeployments.stream()
                .filter(d -> d.getStatus().equalsIgnoreCase("success")
                        || d.getStatus().equalsIgnoreCase("failed"))
                .map(d -> new aquaraga.metrics.model.Deployment(d.getStatus(),
                        d.getDeployable().getCommit().getId(),
                          Date.from(LocalDateTime.parse(d.getUpdated_at().substring(0, d.getUpdated_at().indexOf('.'))).atZone(ZoneId.systemDefault()).toInstant())))
                .collect(Collectors.toList()));
    }

    @Override
    public Commits fetchCommits(DurationWindow durationWindow) {
        int pageNumber = 1;
        List<Commit> commitsForPage;
        List<Commit> allCommits = new ArrayList<>();
        do {
            commitsForPage = getCommitsForPage(String.format("%s/repository/commits?per_page=100&page=%d&ref_name=master&since=%s&until=%s",
                    ciConfiguration.getProjectAPIUrl(),
                    pageNumber,
                    GITLAB_DATE_TIME_FORMATTER.format(durationWindow.beginning()),
                    GITLAB_DATE_TIME_FORMATTER.format(durationWindow.end())));
            allCommits.addAll(commitsForPage);
            pageNumber++;
        } while(commitsForPage.size() == 100);

        return new Commits(allCommits.stream()
                .map(c -> new aquaraga.metrics.model.Commit(c.getId(),
                        Date.from(LocalDateTime.parse(c.getCommitted_date().substring(0, c.getCommitted_date().indexOf('.'))).atZone(ZoneId.systemDefault()).toInstant()),
                        c.getParent_ids()))
                .collect(Collectors.toList()));
    }

    @Override
    public Deployments successfulDeploymentsPreceding(aquaraga.metrics.model.Deployment deployment) {

        String deploymentsUrl = String.format("%s/deployments?per_page=100&page=1&status=success&environment=%s&sort=desc&order_by=created_at&updated_before=%s",
                ciConfiguration.getProjectAPIUrl(),
                ciConfiguration.getProdEnvironmentName(),
                GITLAB_DATE_TIME_FORMATTER.format(deployment.timeDeployed().toInstant()));

        var client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(deploymentsUrl))
                .header("PRIVATE-TOKEN", ciConfiguration.getApiToken())
                .GET()
                .build();
        Deployment[] allDeployments;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException(String.format("Response code for %s is %d", deploymentsUrl, response.statusCode()));
            }
            allDeployments = new Gson().fromJson(response.body(), Deployment[].class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error when fetching deployments from Gitlab", e);
        }
        return new Deployments(Arrays.stream(allDeployments)
                .map(d -> new aquaraga.metrics.model.Deployment(d.getStatus(),
                        d.getDeployable().getCommit().getId(),
                        Date.from(LocalDateTime.parse(d.getUpdated_at().substring(0, d.getUpdated_at().indexOf('.'))).atZone(ZoneId.systemDefault()).toInstant())))
                .collect(Collectors.toList()));
    }

    @Override
    public Commits fetchCommitsBetween(String olderSha, String newerSha) {
        int pageNumber = 1;
        List<Commit> commitsForPage;
        List<Commit> allCommits = new ArrayList<>();
        do {
            commitsForPage = getCommitsForPage(String.format("%s/repository/commits?per_page=100&page=%d&ref_name=%s..%s",
                    ciConfiguration.getProjectAPIUrl(),
                    pageNumber,
                    olderSha,
                    newerSha));
            allCommits.addAll(commitsForPage);
            pageNumber++;
        } while(commitsForPage.size() == 100);

        return new Commits(allCommits.stream()
                .map(c -> new aquaraga.metrics.model.Commit(c.getId(),
                        Date.from(LocalDateTime.parse(c.getCommitted_date().substring(0, c.getCommitted_date().indexOf('.'))).atZone(ZoneId.systemDefault()).toInstant()),
                        c.getParent_ids()))
                .collect(Collectors.toList()));
    }

    private List<Commit> getCommitsForPage(String commitsUrl) {
        var client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(commitsUrl))
                .header("PRIVATE-TOKEN", ciConfiguration.getApiToken())
                .GET()
                .build();
        Commit[] commitsFromGitlab;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException(String.format("Response code for %s is %d", commitsUrl, response.statusCode()));
            }
            commitsFromGitlab = new Gson().fromJson(response.body(), Commit[].class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error when fetching commits from Gitlab", e);
        }
        return Arrays.asList(commitsFromGitlab);
    }

    private List<Deployment> getDeploymentsForPage(int pageNumber, DurationWindow durationWindow) {
        String deploymentsUrl = String.format("%s/deployments?per_page=100&page=%d&environment=%s&sort=desc&order_by=created_at&updated_after=%s&updated_before=%s",
                ciConfiguration.getProjectAPIUrl(),
                pageNumber,
                ciConfiguration.getProdEnvironmentName(),
                GITLAB_DATE_TIME_FORMATTER.format(durationWindow.beginning()),
                GITLAB_DATE_TIME_FORMATTER.format(durationWindow.end()));

        var client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(deploymentsUrl))
                .header("PRIVATE-TOKEN", ciConfiguration.getApiToken())
                .GET()
                .build();
        Deployment[] deploymentsFromGitlab;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RuntimeException(String.format("Response code for %s is %d", deploymentsUrl, response.statusCode()));
            }
            deploymentsFromGitlab = new Gson().fromJson(response.body(), Deployment[].class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error when fetching deployments from Gitlab", e);
        }
        return Arrays.asList(deploymentsFromGitlab);
    }

}
