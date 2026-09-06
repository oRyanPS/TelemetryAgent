package br.com.oryanps.agent;

import br.com.oryanps.agent.api.TelemetryClient;
import br.com.oryanps.agent.config.AgentConfig;
import br.com.oryanps.agent.dto.TelemetryCollector;
import br.com.oryanps.agent.dto.TelemetryData;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class TelemetryAgentApp {

    public static AgentConfig config;
    public static void main(String[] args) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            config = mapper.readValue(
                    new File("config.json"),
                    AgentConfig.class
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        TelemetryCollector collector = new TelemetryCollector();
        TelemetryClient client = new TelemetryClient("http://localhost:8000");
        TelemetryData data = collector.collect();
        client.send(data);
    }
}
