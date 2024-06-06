package otter.jet.monitoring;

public record StreamDetailsResponse(
    String name, StreamConfigResponse config, StreamStateResponse state) {}
