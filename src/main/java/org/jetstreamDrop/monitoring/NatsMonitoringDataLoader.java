package org.jetstreamDrop.monitoring;

public interface NatsMonitoringDataLoader {
  boolean isMonitoringEnabled();

  MonitoringData getMonitoringData();
}
