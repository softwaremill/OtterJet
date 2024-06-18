package otter.jet.monitoring;

public sealed interface MonitoringData
    permits DirectMonitoringResponse, MonitoringNotConfiguredResponse {}
