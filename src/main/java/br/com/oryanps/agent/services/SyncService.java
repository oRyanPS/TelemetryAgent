package br.com.oryanps.agent.services;

import br.com.oryanps.agent.core.enums.TelemetryEndpoint;
import br.com.oryanps.agent.core.interfaces.IService;
import br.com.oryanps.agent.dto.collectors.SyncCollector;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static br.com.oryanps.agent.TelemetryAgentApp.httpClient;
import static br.com.oryanps.agent.TelemetryAgentApp.logger;

public class SyncService implements IService {
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public void run() {
        logger.info("Starting SyncService...");
        SyncCollector collector = new SyncCollector();
        executorService.scheduleAtFixedRate(() -> {
            try {

                httpClient.send(TelemetryEndpoint.SYNC, collector.collect());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.HOURS);
    }
}
