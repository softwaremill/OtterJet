package otterjet.monitoring;

public interface NatsMonitoringDataLoader {
  boolean isMonitoringEnabled();

  MonitoringData getMonitoringData();
}
