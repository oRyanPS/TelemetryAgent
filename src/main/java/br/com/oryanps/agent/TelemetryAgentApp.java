package br.com.oryanps.agent;

import br.com.oryanps.agent.dto.TelemetryData;

public class TelemetryAgentApp {
    public static void main(String[] args) {
        TelemetryCollector collector = new TelemetryCollector();
        TelemetryClient client = new TelemetryClient("http://localhost:8000");
        TelemetryData data = collector.collect();
        client.send(data);
    }
}
