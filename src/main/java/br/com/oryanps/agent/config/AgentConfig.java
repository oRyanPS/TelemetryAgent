package br.com.oryanps.agent.config;

import lombok.Getter;

@Getter
public class AgentConfig {

    private String agendId;
    private String apiUrl;
    private long telemetryInterval;
}
