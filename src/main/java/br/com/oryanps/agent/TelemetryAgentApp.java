package br.com.oryanps.agent;

import br.com.oryanps.agent.api.HttpClientImpl;
import br.com.oryanps.agent.config.AgentConfig;
import br.com.oryanps.agent.config.ConfigManager;
import br.com.oryanps.agent.dto.collectors.HeartbeatCollector;
import br.com.oryanps.agent.services.HeartbeatService;
import br.com.oryanps.agent.services.SyncService;
import lombok.Getter;

import java.util.logging.Level;
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
        httpClient =
                new HttpClientImpl(config.getApiUrl(), config.getAgentId());

        logger.log(Level.INFO, "TelemetryAgentApp started");

        new HeartbeatService().run();
        new SyncService().run();


    }
}
