package otterjet.monitoring;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

interface NatsMonitoringApiClient {
  @RequestMapping(method = RequestMethod.GET, value = "/jsz?streams=true&config=true")
  JetStreamMonitoringResponse getJetStreamMonitoringData();
}
