package br.com.oryanps.agent.api;

import br.com.oryanps.agent.dto.TelemetryData;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TelemetryClient {
    private final HttpClient client;
    private final ObjectMapper mapper;

    private final String apiUrl;

    public TelemetryClient(String apiUrl) {
        this.apiUrl = apiUrl;

        client = HttpClient.newHttpClient();

        mapper = new ObjectMapper();
    }

    public void send(TelemetryData data) {
        try {
            String json =
                    mapper.writeValueAsString(data);

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create(
                                    apiUrl + "/api/telemetry"
                            ))
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(json)
                            )
                            .build();
            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers
                                    .ofString()
                    );

            System.out.println(
                    "API: " + response.statusCode()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
