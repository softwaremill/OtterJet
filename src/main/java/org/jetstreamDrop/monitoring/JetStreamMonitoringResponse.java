package org.jetstreamDrop.monitoring;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record JetStreamMonitoringResponse(
    int streams,
    int consumers,
    long messages,
    @JsonProperty("account_details") List<AccountDetailsResponse> accountDetails) {}
