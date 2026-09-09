package br.com.oryanps.agent;

import br.com.oryanps.agent.api.HttpClientImpl;
import br.com.oryanps.agent.core.enums.TelemetryEndpoint;
import br.com.oryanps.agent.config.AgentConfig;
import br.com.oryanps.agent.config.ConfigManager;
import br.com.oryanps.agent.dto.collectors.FullCollector;
import br.com.oryanps.agent.dto.collectors.TelemetryCollector;
import lombok.Getter;
import lombok.var;

public class TelemetryAgentApp {

    @Getter
    public static AgentConfig config;

    public static void main(String[] args)
            throws Exception {

        ConfigManager configManager =
                new ConfigManager();
        config =
                configManager.load();
        TelemetryCollector telemetryCollector =
                new TelemetryCollector();
        FullCollector heartbeatCollector =
                new FullCollector();
        HttpClientImpl httpClient =
                new HttpClientImpl(config.getApiUrl(), config.getAgentId());
        System.out.println("mTadata Agent Iniciado");

        // TODO: Criar handlers para cada log separado.
        while (true) {
            try {
                var heartbeat =
                        heartbeatCollector.collect();

                httpClient.send(TelemetryEndpoint.TEST, heartbeat);

                Thread.sleep(5000);

            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(10000);
            }
        }
    }
}
