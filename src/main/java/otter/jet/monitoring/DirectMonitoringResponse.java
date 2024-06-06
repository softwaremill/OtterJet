package otter.jet.monitoring;

public record DirectMonitoringResponse(JetStreamMonitoringResponse response)
    implements MonitoringData {}
