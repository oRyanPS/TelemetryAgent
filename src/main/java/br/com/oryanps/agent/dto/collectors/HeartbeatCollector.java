package br.com.oryanps.agent.dto.collectors;

import br.com.oryanps.agent.core.interfaces.ICollector;
import br.com.oryanps.agent.dto.HeartbeatData;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.Sensors;
import oshi.software.os.OperatingSystem;

import static br.com.oryanps.agent.TelemetryAgentApp.config;

public class HeartbeatCollector implements ICollector<HeartbeatData> {

    private final SystemInfo systemInfo;

    private final CentralProcessor processor;
    private final GlobalMemory memory;
    private final OperatingSystem operatingSystem;
    private final Sensors sensors;

    private long[] previousCpuTicks;

    public HeartbeatCollector() {
        systemInfo = new SystemInfo();

        processor = systemInfo.getHardware().getProcessor();
        memory = systemInfo.getHardware().getMemory();
        operatingSystem = systemInfo.getOperatingSystem();
        sensors = systemInfo.getHardware().getSensors();
        previousCpuTicks = processor.getSystemCpuLoadTicks();
    }

    public HeartbeatData collect() {
        HeartbeatData data = new HeartbeatData();

        data.setAgentId(config.getAgentId());
        data.setSystemUptime(operatingSystem.getSystemUptime());

        double cpu = processor.getSystemCpuLoadBetweenTicks(previousCpuTicks) * 100;
        previousCpuTicks = processor.getSystemCpuLoadTicks();
        data.setCpuUsage(cpu);
        data.setCpuTemperature(
                sensors.getCpuTemperature() > 0 ? sensors.getCpuTemperature() : null
        );
        data.setCpuVoltage(
                sensors.getCpuVoltage() > 0 ? sensors.getCpuVoltage() : null
        );
        data.setCpuFanSpeed(
                sensors.getFanSpeeds()[0] > 0 ? sensors.getFanSpeeds() : null
        );

        data.setRamTotal(memory.getTotal());
        data.setRamUsed(memory.getTotal() - memory.getAvailable());

        return data;
    }



}
