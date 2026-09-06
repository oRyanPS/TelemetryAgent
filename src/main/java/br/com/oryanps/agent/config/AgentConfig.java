package br.com.oryanps.agent.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AgentConfig {

    private String agendId;
    private String apiUrl;
    private long telemetryInterval;
}
