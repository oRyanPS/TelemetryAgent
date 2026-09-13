package br.com.oryanps.agent.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HeartbeatData {
    private String agentId;
    private long systemUptime;

    private Double cpuUsage;
    private Double cpuTemperature;
    private Double cpuVoltage;
    private int[] cpuFanSpeed;

    private long ramTotal;
    private long ramUsed;

}
