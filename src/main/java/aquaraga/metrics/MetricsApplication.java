package aquaraga.metrics;

import aquaraga.metrics.config.CIConfiguration;
import aquaraga.metrics.model.DurationWindow;
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
	@Autowired
	CIConfiguration ciConfiguration;

	public static void main(String[] args) {
		SpringApplication.run(MetricsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		pointInTime();
		trend();
//		forWindow();
	}

	private void forWindow() {
		FourKeyMetrics metrics = fourKeyMetricsService
				.metrics(new DurationWindow(14,140));
		reportService.consoleOut(metrics);
	}

	private void trend() {
		//Report metrics every 14 days for the last 140 days
		for (int shiftLeft = 140; shiftLeft >= 0; shiftLeft-=14) {
			DurationWindow durationWindow =
					new DurationWindow(14, shiftLeft);
			FourKeyMetrics metrics = fourKeyMetricsService.metrics(
					durationWindow);
			reportService.consoleOutTrend(durationWindow, metrics);
		}
	}

	private void pointInTime() {
		FourKeyMetrics metrics = fourKeyMetricsService.metrics();
		reportService.consoleOut(metrics);
	}
}
