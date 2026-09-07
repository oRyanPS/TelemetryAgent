package br.com.oryanps.agent;

import br.com.oryanps.agent.api.TelemetryClient;
import br.com.oryanps.agent.config.AgentConfig;
import br.com.oryanps.agent.config.ConfigManager;
import br.com.oryanps.agent.dto.TelemetryCollector;
import lombok.var;

public class TelemetryAgentApp {


    public static void main(String[] args)
            throws Exception {

        ConfigManager configManager =
                new ConfigManager();
        AgentConfig config =
                configManager.load();
        TelemetryCollector collector =
                new TelemetryCollector();
        TelemetryClient telemetryClient =
                new TelemetryClient(config.getApiUrl());
        System.out.println("mTadata Agent Iniciado");
        while (true) {
            try {
                var telemetry =
                        collector.collect();

                telemetryClient.send(telemetry);

                Thread.sleep(5000);

            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(10000);
            }
        }
    }
}
