package org.jetstreamDrop.monitoring;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StreamStateResponse(long messages, @JsonProperty("consumer_count") int consumers) {}
