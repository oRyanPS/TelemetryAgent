package br.com.oryanps.agent.dto.collectors;

import br.com.oryanps.agent.core.interfaces.ICollector;
import br.com.oryanps.agent.dto.SyncData;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import static br.com.oryanps.agent.TelemetryAgentApp.config;

public class SyncCollector implements ICollector<SyncData> {

    private final SystemInfo systemInfo;

    private final CentralProcessor processor;
    private final GlobalMemory memory;
    private final OperatingSystem operatingSystem;

    public SyncCollector() {
        systemInfo = new SystemInfo();

        processor = systemInfo.getHardware().getProcessor();
        memory = systemInfo.getHardware().getMemory();
        operatingSystem = systemInfo.getOperatingSystem();
    }

    @Override
    public SyncData collect() {
        SyncData data = new SyncData();

        // Dados Gerais
        data.setAgentId(config.getAgentId());
        data.setHostname(operatingSystem.getNetworkParams().getHostName());
        data.setDomainName(operatingSystem.getNetworkParams().getDomainName());

        // ‘Hardware’
        // Verificação de CPU.
        data.setCpuName(processor.getProcessorIdentifier().getName());

        // Verificação de RAM.
        data.setPhysicalMemoryList(memory.getPhysicalMemory());
        data.setRamTotal(memory.getTotal());

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

        // Dados de USB.
        data.setUsbDevices(systemInfo.getHardware().getUsbDevices(true));

        // Dados de Rede.
        data.setNetworkIFList(systemInfo.getHardware().getNetworkIFs());

        // Dados do S.O.
        data.setOperatingSystem(operatingSystem.toString());
        data.setSystemUptime(operatingSystem.getSystemUptime());
        data.setServiceList(operatingSystem.getServices());
        data.setApplicationInfoList(operatingSystem.getInstalledApplications());

        data.setTimestamp(System.currentTimeMillis());
        return data;
    }
}
