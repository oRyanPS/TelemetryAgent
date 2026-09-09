package br.com.oryanps.agent.dto.collectors;

import br.com.oryanps.agent.core.interfaces.ICollector;
import br.com.oryanps.agent.dto.FullData;
import lombok.AllArgsConstructor;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import static br.com.oryanps.agent.TelemetryAgentApp.config;

public class FullCollector implements ICollector<FullData> {

    private final SystemInfo systemInfo;

    private final CentralProcessor processor;
    private final GlobalMemory memory;
    private final OperatingSystem operatingSystem;

    private long[] previousCpuTicks;

    public FullCollector() {
        systemInfo = new SystemInfo();

        processor = systemInfo.getHardware().getProcessor();
        memory = systemInfo.getHardware().getMemory();
        operatingSystem = systemInfo.getOperatingSystem();
        previousCpuTicks = processor.getSystemCpuLoadTicks();
    }

    @Override
    public FullData collect() {
        FullData data = new FullData();

        // Dados Gerais
        data.setAgentId(config.getAgendId());
        data.setHostname(operatingSystem.getNetworkParams().getHostName());
        data.setDomainName(operatingSystem.getNetworkParams().getDomainName());


        // Verificação de Discos de armazenamento.
        data.setPhysicalDisks(systemInfo.getHardware().getDiskStores());
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

        // Verificação de CPU.
        data.setCpuName(processor.getProcessorIdentifier().getName());
        double cpu = processor.getSystemCpuLoadBetweenTicks(previousCpuTicks) * 100;
        previousCpuTicks = processor.getSystemCpuLoadTicks();
        data.setCpuUsage(cpu);

        // Verificação de RAM.
        data.setPhysicalMemoryList(memory.getPhysicalMemory());
        data.setRamTotal(memory.getTotal());
        data.setRamUsed(memory.getTotal() - memory.getAvailable());

        // Dados do S.O.
        data.setOperatingSystem(operatingSystem.toString());
        data.setSystemUptime(operatingSystem.getSystemUptime());
        data.setServiceList(operatingSystem.getServices());
        data.setApplicationInfoList(operatingSystem.getInstalledApplications());

        // Dados de Hardware USB.
        data.setUsbDevices(systemInfo.getHardware().getUsbDevices(true));

        // Dados de Rede.
        data.setNetworkIFList(systemInfo.getHardware().getNetworkIFs());

        data.setTimestamp(System.currentTimeMillis());
        return data;
    }
}
