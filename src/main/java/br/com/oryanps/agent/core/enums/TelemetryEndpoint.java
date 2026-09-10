package br.com.oryanps.agent.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TelemetryEndpoint {
    TEST("/api/telemetry"),
    HEARTBEAT("/v1/telemetry/heartbeat"),
    BATCH("/v1/telemetry/batch"),
    EVENTS("/v1/telemetry/events"),
    SYNC("/v1/telemetry/sync");

    private final String path;
}
