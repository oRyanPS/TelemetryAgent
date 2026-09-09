package br.com.oryanps.agent.dto.collectors;

import br.com.oryanps.agent.core.interfaces.ICollector;
import br.com.oryanps.agent.dto.HeartbeatData;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OperatingSystem;

import static br.com.oryanps.agent.TelemetryAgentApp.config;

public class HeartbeatCollector implements ICollector<HeartbeatData> {

    private final SystemInfo systemInfo;

    private final CentralProcessor processor;
    private final GlobalMemory memory;
    private final OperatingSystem operatingSystem;

    private long[] previousCpuTicks;

    public HeartbeatCollector() {
        systemInfo = new SystemInfo();

        processor = systemInfo.getHardware().getProcessor();
        memory = systemInfo.getHardware().getMemory();
        operatingSystem = systemInfo.getOperatingSystem();
        previousCpuTicks = processor.getSystemCpuLoadTicks();
    }

    public HeartbeatData collect() {
        HeartbeatData data = new HeartbeatData();

        data.setAgentId(config.getAgendId());
        data.setSystemUptime(operatingSystem.getSystemUptime());

        double cpu = processor.getSystemCpuLoadBetweenTicks(previousCpuTicks) * 100;
        previousCpuTicks = processor.getSystemCpuLoadTicks();
        data.setCpuUsage(cpu);

        data.setRamTotal(memory.getTotal());
        data.setRamUsed(memory.getTotal() - memory.getAvailable());

        return data;
    }



}
