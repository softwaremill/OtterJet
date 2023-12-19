package org.jetstreamDrop.monitoring;

public record DirectMonitoringResponse(JetStreamMonitoringResponse response)
    implements MonitoringData {}
