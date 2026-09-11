package br.com.oryanps.agent;

import br.com.oryanps.agent.api.HttpClientImpl;
import br.com.oryanps.agent.config.AgentConfig;
import br.com.oryanps.agent.config.ConfigManager;
import br.com.oryanps.agent.core.enums.TelemetryEndpoint;
import br.com.oryanps.agent.dto.collectors.HeartbeatCollector;
import br.com.oryanps.agent.dto.collectors.TelemetryCollector;
import br.com.oryanps.agent.services.HeartbeatService;
import br.com.oryanps.agent.services.SyncService;
import lombok.Getter;
import lombok.var;

import java.util.logging.Logger;

public class TelemetryAgentApp {

    @Getter
    public static AgentConfig config;
    @Getter
    public static HttpClientImpl httpClient;
    @Getter
    public static Logger logger;

    public static void main(String[] args)
            throws Exception {

        ConfigManager configManager =
                new ConfigManager();
        config =
                configManager.load();
        logger =
                Logger.getLogger(TelemetryAgentApp.class.getName());
        HeartbeatCollector heartbeatCollector =
                new HeartbeatCollector();

        httpClient =
                new HttpClientImpl(config.getApiUrl(), config.getAgentId());
        System.out.println("mTadata Agent Iniciado");

        // TODO: Criar handlers para cada log separado.

        new HeartbeatService().run();
        new SyncService().run();
        /*
        while (true) {
            try {
                var heartbeat =
                        heartbeatCollector.collect();

                httpClient.send(TelemetryEndpoint.HEARTBEAT, heartbeat);

                Thread.sleep(5000);

            } catch (Exception e) {
                e.printStackTrace();
                Thread.sleep(10000);
            }
        }

         */

    }
}
