package br.com.oryanps.agent.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TelemetryData {
    private String agentId;
    private String hostname;
    private String operatingSystem;

    private String cpuName;
    private double cpuUsage;

    private long ramTotal;
    private long ramUsed;

    private long diskTotal;
    private long diskUsed;

    private long timestamp;

    @Override
    public String toString() {
        return "TelemetryData{" +
                "agentId='" + agentId + '\'' +
                ", hostname='" + hostname + '\'' +
                ", operatingSystem='" + operatingSystem + '\'' +
                ", cpuName='" + cpuName + '\'' +
                ", cpuUsage=" + String.format("%.2f%%", cpuUsage) +
                ", ramTotal=" + (ramTotal / (1024 * 1024 * 1024)) + " GB" +
                ", ramUsed=" + (ramUsed / (1024 * 1024 * 1024)) + " GB" +
                ", diskTotal=" + (diskTotal / (1024 * 1024 * 1024)) + " GB" +
                ", diskUsed=" + (diskUsed / (1024 * 1024 * 1024)) + " GB" +
                ", timestamp=" + timestamp +
                '}';
    }
}


