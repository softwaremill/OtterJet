package otter.jet.monitoring;

public interface NatsMonitoringDataLoader {
  boolean isMonitoringEnabled();

  MonitoringData getMonitoringData();
}
