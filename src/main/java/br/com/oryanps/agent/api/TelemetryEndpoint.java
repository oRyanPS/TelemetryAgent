package br.com.oryanps.agent.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TelemetryEndpoint {
    TEST("/api/telemetry"),
    HEARTBEAT("/v1/telemetry/heartbeat"),
    BATCH("/v1/telemetry/batch"),
    EVENTS("/v1/telemetry/events"),
    INVENTORY("/v1/telemetry/inventory");

    private final String path;
}
