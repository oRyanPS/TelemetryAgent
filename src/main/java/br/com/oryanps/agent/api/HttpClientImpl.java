package br.com.oryanps.agent.api;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpClientImpl {

    private final String baseUrl;
    private final String agentId;
    private final ObjectMapper objectMapper;
    private final ExecutorService executorService;

    public HttpClientImpl(String baseUrl, String agentId) {
        this.baseUrl = baseUrl;
        this.agentId = agentId;
        this.objectMapper = new ObjectMapper();
        this.executorService = Executors.newFixedThreadPool(4);
    }

    /**
     * Envia assincronamente telemetria para os endpoints.
     *
     * @param endpoint Enum referente ao caminho da rota
     * @param payload Objeto com dados de telemetria
     * @param <T> DTO
     * @return CompletableFuture contendo o código HTTP de retorno.
     */

    public <T> CompletableFuture<Integer> send(TelemetryEndpoint endpoint, T payload) {
        CompletableFuture<Integer> future = new CompletableFuture<>();

        executorService.submit(() -> {
            HttpURLConnection connection = null;

            try {
                String jsonBody = objectMapper.writeValueAsString(payload);

                URL url = new URL(baseUrl+endpoint.getPath());
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty(
                        "Content-Type",
                        "application/json; utf-8"
                );
                connection.setRequestProperty(
                        "Accept",
                        "application/json"
                );
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setDoOutput(true);

                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }

                int responseCode = connection.getResponseCode();
                future.complete(responseCode);
            } catch (Exception e) {
                future.completeExceptionally(e);
            } finally {
                if(connection != null) {
                    connection.disconnect();
                }
            }
        });

        return future;
    }

    public void shutdown() {
        this.executorService.shutdown();
    }
}
