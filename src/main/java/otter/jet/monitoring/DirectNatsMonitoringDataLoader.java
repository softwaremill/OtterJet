package otter.jet.monitoring;

class DirectNatsMonitoringDataLoader implements NatsMonitoringDataLoader {

  private final NatsMonitoringApiClient natsMonitoringApiClient;

  DirectNatsMonitoringDataLoader(NatsMonitoringApiClient natsMonitoringApiClient) {
    this.natsMonitoringApiClient = natsMonitoringApiClient;
  }

  @Override
  public boolean isMonitoringEnabled() {
    return true;
  }

  @Override
  public MonitoringData getMonitoringData() {
    return new DirectMonitoringResponse(natsMonitoringApiClient.getJetStreamMonitoringData());
  }
}
