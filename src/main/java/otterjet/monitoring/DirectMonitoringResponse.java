package otterjet.monitoring;

public record DirectMonitoringResponse(JetStreamMonitoringResponse response)
    implements MonitoringData {}
