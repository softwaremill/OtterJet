package otterjet.monitoring;

import org.springframework.web.bind.annotation.GetMapping;

interface NatsMonitoringApiClient {
  @GetMapping("/jsz?streams=true&config=true")
  JetStreamMonitoringResponse getJetStreamMonitoringData();
}
