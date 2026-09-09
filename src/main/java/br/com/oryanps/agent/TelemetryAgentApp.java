package br.com.oryanps.agent;

import br.com.oryanps.agent.api.HttpClientImpl;
import br.com.oryanps.agent.api.TelemetryClient;
import br.com.oryanps.agent.api.TelemetryEndpoint;
import br.com.oryanps.agent.config.AgentConfig;
import br.com.oryanps.agent.config.ConfigManager;
import br.com.oryanps.agent.dto.collectors.HeartbeatCollector;
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
        HeartbeatCollector heartbeatCollector =
                new HeartbeatCollector();
        TelemetryClient telemetryClient =
                new TelemetryClient(config.getApiUrl());
        HttpClientImpl httpClient =
                new HttpClientImpl(config.getApiUrl(), config.getAgendId());
        System.out.println("mTadata Agent Iniciado");
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
