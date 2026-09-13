package br.com.oryanps.agent.services;

import br.com.oryanps.agent.core.enums.TelemetryEndpoint;
import br.com.oryanps.agent.core.interfaces.IService;
import br.com.oryanps.agent.dto.collectors.HeartbeatCollector;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static br.com.oryanps.agent.TelemetryAgentApp.httpClient;
import static br.com.oryanps.agent.TelemetryAgentApp.logger;

public class HeartbeatService implements IService {

    private final ScheduledExecutorService executorService =
            Executors.newSingleThreadScheduledExecutor();

    public void run() {

        logger.info("Starting HeartbeatService...");
        HeartbeatCollector heartbeat =
                new HeartbeatCollector();

        executorService.scheduleAtFixedRate(() -> {

            try {
                httpClient.send(
                        TelemetryEndpoint.HEARTBEAT,
                        heartbeat.collect()
                ).thenAccept(responseCode -> {

                }).exceptionally(error -> {

                    logger.severe(
                            "Heartbeat failed: "
                                    + error.getMessage()
                    );

                    error.printStackTrace();

                    return null;
                });

            } catch (Exception e) {

                logger.severe(
                        "Heartbeat service error: "
                                + e.getMessage()
                );

                e.printStackTrace();
            }

        }, 0, 5, TimeUnit.SECONDS);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}