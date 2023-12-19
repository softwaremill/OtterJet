package org.jetstreamDrop.monitoring;

public sealed interface MonitoringData
    permits DirectMonitoringResponse, MonitoringNotConfiguredResponse {}
