package br.com.oryanps.agent.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HeartbeatData {
    private String agentId;
    private long systemUptime;

    private double cpuUsage;
    private long ramTotal;
    private long ramUsed;

}
