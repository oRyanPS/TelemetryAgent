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
public class SyncData implements IData {
    private String agentId;

    private String domainName;
    private String hostname;

    private String operatingSystem;
    private List<OSService> serviceList;
    private List<ApplicationInfo> applicationInfoList;
    private long systemUptime;

    private String cpuName;
    private double cpuUsage;

    private List<PhysicalMemory> physicalMemoryList;
    private long ramTotal;
    private long ramUsed;

    private List<HWDiskStore> physicalDisks;
    private long diskTotal;
    private long diskUsed;

    private List<NetworkIF> networkIFList;
    private List<UsbDevice> usbDevices;

    private long timestamp;
}
