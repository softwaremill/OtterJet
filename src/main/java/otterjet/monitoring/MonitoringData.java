package otterjet.monitoring;

public sealed interface MonitoringData
    permits DirectMonitoringResponse, MonitoringNotConfiguredResponse {}
