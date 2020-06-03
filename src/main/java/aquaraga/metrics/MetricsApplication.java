package aquaraga.metrics;

import aquaraga.metrics.model.FourKeyMetrics;
import aquaraga.metrics.service.FourKeyMetricsService;
import aquaraga.metrics.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MetricsApplication implements CommandLineRunner {

	@Autowired
	FourKeyMetricsService fourKeyMetricsService;
	@Autowired
	ReportService reportService;

	public static void main(String[] args) {
		SpringApplication.run(MetricsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		FourKeyMetrics metrics = fourKeyMetricsService.metrics();
		reportService.consoleOut(metrics);
	}
}
