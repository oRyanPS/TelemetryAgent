package br.com.oryanps.agent;

import br.com.oryanps.agent.dto.TelemetryData;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

public class TelemetryCollector {

    private final SystemInfo systemInfo;

    private final CentralProcessor processor;
    private final GlobalMemory memory;
    private final OperatingSystem operatingSystem;

    private long[] previousCpuTicks;

    public TelemetryCollector() {
        systemInfo = new SystemInfo();

        processor = systemInfo.getHardware().getProcessor();
        memory = systemInfo.getHardware().getMemory();
        operatingSystem = systemInfo.getOperatingSystem();
        previousCpuTicks = processor.getSystemCpuLoadTicks();
    }

    public TelemetryData collect() {
        TelemetryData data = new TelemetryData();

        data.setAgentId("PC-001");
        data.setHostname(
                operatingSystem
                        .getNetworkParams().getHostName()
        );

        data.setOperatingSystem(operatingSystem.toString());
        data.setCpuName(processor.getProcessorIdentifier().getName());

        double cpu = processor.getSystemCpuLoadBetweenTicks(previousCpuTicks) * 100;
        previousCpuTicks = processor.getSystemCpuLoadTicks();
        data.setCpuUsage(cpu);

        data.setRamTotal(memory.getTotal());
        data.setRamUsed(memory.getTotal() - memory.getAvailable());

        FileSystem fileSystem =
                operatingSystem.getFileSystem();

        long total = 0;
        long usable = 0;

        for (OSFileStore store :
                fileSystem.getFileStores()) {
            total += store.getTotalSpace();
            usable += store.getUsableSpace();
        }

        data.setDiskTotal(total);
        data.setDiskUsed(total - usable);

        data.setTimestamp(System.currentTimeMillis());


        System.out.println(data.toString());
        return data;
    }


}
