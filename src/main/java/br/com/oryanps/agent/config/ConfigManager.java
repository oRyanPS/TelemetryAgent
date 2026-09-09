package br.com.oryanps.agent.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class ConfigManager {
    private static final String CONFIG_DIRECTORY =
            "C:\\ProgramData\\mTadata Agent";
    // TODO: Corrigir criação de yml para json no Executável.
    private static final String CONFIG_FILE =
            CONFIG_DIRECTORY + "\\config.json";

    private final ObjectMapper mapper =
            new ObjectMapper();


    public AgentConfig load() throws Exception {
        File file = new File(CONFIG_FILE);
        if(!file.exists()) {
            save(new AgentConfig());
        }

        return mapper.readValue(
                file,
                AgentConfig.class
        );
    }

    public void save(AgentConfig config) throws Exception {
        File directory = new File(CONFIG_DIRECTORY);

        if(!directory.exists()) {
            directory.mkdirs();
        }

        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(
                        new File(CONFIG_FILE),
                        config
                );
    }
}
