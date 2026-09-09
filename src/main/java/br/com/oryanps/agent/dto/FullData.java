package br.com.oryanps.agent.dto;

import br.com.oryanps.agent.core.interfaces.IData;
import lombok.Getter;
import lombok.Setter;
import oshi.hardware.HWDiskStore;
import oshi.hardware.NetworkIF;
import oshi.hardware.PhysicalMemory;
import oshi.hardware.UsbDevice;
import oshi.software.os.ApplicationInfo;
import oshi.software.os.OSService;

import java.util.List;

@Getter
@Setter
public class FullData extends TelemetryData implements IData {
    private String agentId;

    private String operatingSystem;
    private long systemUptime;
    private String hostname;
    private String domainName;


    private String cpuName;
    private List<PhysicalMemory> physicalMemoryList;
    private List<HWDiskStore> physicalDisks;


    private List<NetworkIF> networkIFList;
    private List<OSService> serviceList;
    private List<ApplicationInfo> applicationInfoList;
    private List<UsbDevice> usbDevices;

}
