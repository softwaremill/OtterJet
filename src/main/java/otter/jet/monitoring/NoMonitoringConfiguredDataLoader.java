package otter.jet.monitoring;

class NoMonitoringConfiguredDataLoader implements NatsMonitoringDataLoader {
  @Override
  public boolean isMonitoringEnabled() {
    return false;
  }

  @Override
  public MonitoringData getMonitoringData() {
    return new MonitoringNotConfiguredResponse();
  }
}
