package br.com.oryanps.agent.api;

import br.com.oryanps.agent.dto.TelemetryData;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class TelemetryClient {
    private final ObjectMapper mapper;
    private final String apiUrl;

    public TelemetryClient(String apiUrl) throws IOException {
        this.apiUrl = apiUrl;
        mapper = new ObjectMapper();
    }

    public void send(TelemetryData data) {
        HttpURLConnection connection = null;

        try {
            String json =
                    mapper.writeValueAsString(data);

            URL url = new URL(apiUrl + "/api/telemetry");

            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty(
                    "Content-Type",
                    "application/json"
            );

            connection.setRequestProperty(
                    "Accept",
                    "application/json"
            );

            connection.setDoOutput(true);
            connection.setDoInput(true);

            OutputStream outputStream = connection.getOutputStream();

            try {
                outputStream.write(
                        json.getBytes(StandardCharsets.UTF_8)
                );

                outputStream.flush();
            } finally {
                outputStream.close();
            }

            int statusCode = connection.getResponseCode();
            System.out.println("API: "+statusCode);

            InputStream inputStream;

            if(statusCode >= 200 && statusCode <400) {
                inputStream = connection.getInputStream();
            } else {
                inputStream = connection.getErrorStream();
            }

            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                inputStream,
                                StandardCharsets.UTF_8
                        )
                );

                try {
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    System.out.println("Resposta: "+response.toString());
                } finally {
                    reader.close();
                }
            }

        } catch (Exception e) {
            System.err.println(
                    "Erro ao enviar telemetria para a API:"
            );
            e.printStackTrace();
        } finally {
            if(connection != null)
                connection.disconnect();
        }
    }


}
