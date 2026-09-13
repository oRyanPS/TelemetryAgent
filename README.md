# TelemetryAgent

## Visão geral

O **TelemetryAgent** é um agente de telemetria desktop em Java para Windows. Ele coleta informações do sistema operacional e do hardware local e envia esses dados para uma API remota em formato JSON.

O projeto usa:
- **Java** com Gradle
- **OSHI** para coleta de dados do sistema
- **Jackson** para serialização JSON
- **Lombok** para reduzir código repetitivo
- **Inno Setup** e um serviço Windows para instalação/distribuição

## Funcionalidade do projeto

No fluxo atual do código, o agente faz o seguinte:

1. Lê a configuração local em `config.json`.
2. Inicializa o cliente HTTP com `agentId` e `apiUrl`.
3. Inicia os serviços de coleta:
   - **HeartbeatService**: envia dados a cada 5 segundos.
   - **SyncService**: envia dados a cada 1 hora.
4. Serializa os DTOs em JSON e faz `POST` para a API remota.

### O que é coletado

- Identificação do agente
- Uptime do sistema
- CPU
- Memória RAM
- Nome do host e domínio
- Discos físicos
- Interfaces de rede
- Serviços do sistema
- Aplicações instaladas
- Dispositivos USB
- Informações básicas do sistema operacional

## Estrutura de pastas do projeto
### Saídas de build

- `build/libs/TelemetryAgent.jar`
  - artefato gerado pelo build

## Pastas de instalação

Pelo script de instalação, os principais caminhos são:

### Pasta da aplicação

O instalador usa por padrão:

- `%ProgramFiles%\mTadata Agent`

Nessa pasta ficam os arquivos instalados da aplicação, como:
- `m-tadata-agent.exe`
- `TelemetryAgent.jar`
- `m-tadata-agent.xml`

### Pasta de configuração

A configuração local é criada em:

- `C:\ProgramData\mTadata Agent\config.json`

Essa pasta é usada pelo `ConfigManager` para ler e salvar a configuração do agente.

### Observações da instalação

O instalador (`mTadata.iss`) também:
- cria o `config.json` automaticamente
- registra a aplicação para iniciar como serviço
- executa ações de `install` e `start` após a instalação
- executa `stop` e `uninstall` na remoção

## Configuração do agente

O arquivo `config.json` contém os dados básicos do agente:

```json
{
  "agentId": "home/PC-001",
  "apiUrl": "https://mtadata.com.br/api",
  "telemetryInterval": 0
}
```

### Campos

- `agentId`: identificador do equipamento/agente
- `apiUrl`: URL base do servidor de telemetria
  - o apiUrl deve apontar diretamente para o endereço de entrada da api, se a rota criada no seu webserver for `https://webserver.com/server/v1/telemetry/heartbeat` o endereço configurado deve ser `https://webserver.com/server`
- `telemetryInterval`: intervalo genérico de telemetria usado pela configuração

## Rotas da API consumidas

O agente não expõe uma API própria; ele **consome** endpoints remotos e envia dados via `POST` com `Content-Type: application/json`.

A URL final é montada assim:

```text
{apiUrl}/{path}
```

Exemplo:

```text
https://mtadata.com.br/api/v1/telemetry/heartbeat
```

### Endpoints definidos

| Enum        | Método | Path                      | Uso atual                     |
|-------------|-------:|---------------------------|-------------------------------|
| `HEARTBEAT` | `POST` | `/v1/telemetry/heartbeat` | usada pelo `HeartbeatService` |
| `BATCH`     | `POST` | `/v1/telemetry/batch`     | reservada no enum             |
| `EVENTS`    | `POST` | `/v1/telemetry/events`    | reservada no enum             |
| `SYNC`      | `POST` | `/v1/telemetry/sync`      | usada pelo `SyncService`      |

## JSONs enviados para a API

### 1) Heartbeat

Endpoint:

- `POST /v1/telemetry/heartbeat`

DTO:

- `HeartbeatData`

Campos do payload:

- `agentId`
- `systemUptime`
- `cpuUsage`
- `cpuTemperature`
- `cpuVoltage`
- `ramTotal`
- `ramUsed`

Exemplo:

```json
{
  "agentId": "home/PC-001",
  "systemUptime": 123456,
  "cpuUsage": 18.75,
  "cpuTemperature": 52.4,
  "cpuVoltage": 0.0,
  "ramTotal": 17179869184,
  "ramUsed": 8589934592
}
```

### 2) Sync

Endpoint:

- `POST /v1/telemetry/sync`

DTO:

- `SyncData`

Campos do payload:

- `agentId`
- `operatingSystem`
- `systemUptime`
- `hostname`
- `domainName`
- `cpuName`
- `physicalMemoryList`
- `physicalDisks`
- `networkIFList`
- `serviceList`
- `applicationInfoList`
- `usbDevices`

Exemplo de estrutura:

```json
{
  "agentId": "home/PC-001",
  "operatingSystem": "Microsoft Windows 11 Pro",
  "systemUptime": 123456,
  "hostname": "Liam-PC",
  "domainName": "WORKGROUP",
  "cpuName": "Intel(R) Core(TM) i7...",
  "physicalMemoryList": [
    {
      "bankLabel": "BANK 0",
      "capacity": 8589934592,
      "clockSpeed": 3200000000,
      "manufacturer": "Unknown",
      "memoryType": "DDR4",
      "partNumber": "ALCAMARA",
      "serialNumber": "00000000"
    }
  ],
  "physicalDisks": [
    {
      "name": "C:",
      "model": "SSD...",
      "serial": "NB1111111111",
      "size": 512105932800,
      "reads": 1022768,
      "readBytes": 35071070208,
      "writes": 1124605,
      "writeBytes": 19580055040,
      "currentQueueLength": 0,
      "transferTime": 3655523,
      "timeStamp": 1788920766669,
      "partitions": [
        {
          "identification": "Disco #1, Partição #1",
          "name": "GPT: Basic Data",
          "type": "GPT: dados Básicos",
          "uuid": "684b2fb7-e3bb-4852-919c-e3d45ec4add2",
          "label": "1 - SSD SATA III (500GB)",
          "size": 511139905536,
          "major": 1,
          "minor": 1,
          "mountPoint": "C:\\"
        }
      ]
    }
  ],
  "networkIFList": [
    {
      "name": "Ethernet",
      "displayName": "Intel(R) Ethernet...",
      "index": 15,
      "mtu": 1492,
      "subnetMasks": [
        24
      ],
      "prefixLengths": [
        64,
        128,
        128
      ],
      "ifType": 6,
      "ndisPhysicalMediumType": 14,
      "connectorPresent": true,
      "bytesRecv": 2948782648,
      "bytesSent": 227014986,
      "packetsRecv": 3423320,
      "packetsSent": 1322824,
      "inErrors": 0,
      "outErrors": 0,
      "inDrops": 0,
      "collisions": 0,
      "speed": 100000000,
      "timeStamp": 1789267080028,
      "ifAlias": "Ethernet 1",
      "ifOperStatus": "UP",
      "ipv4addr": [
        "192.168.0.2"
      ],
      "macaddr": "...",
      "ipv6addr": [
        "...",
        "...6"
      ],
      "knownVmMacAddr": false
    }
  ],
  "serviceList": [
    {
      "name": "Spooler",
      "processID": 0,
      "state": "RUNNING"
    }
  ],
  "applicationInfoList": [
    {
      "name": "Google Chrome",
      "version": "152.0.7977.83",
      "vendor": "Google LLC",
      "timestamp": 1788836400000,
      "additionalInfo": {
        "installLocation": "C:\\Program Files\\Google\\Chrome\\Application",
        "installSource": null
      }
    }
  ],
  "usbDevices": [
    {
      "name": "Intel(R) USB 3.0 eXtensible Host Controller - 1.0 (Microsoft)",
      "vendor": "Generic USB xHCI Host Controller",
      "vendorId": "0x8086",
      "productId": "0xa12f",
      "serialNumber": null,
      "uniqueDeviceId": "PCI\\VEN_8086&DEV_A12F&SUBSYS_86941043&REV_31\\3&11583659&0&A0",
      "connectedDevices": []
    }
  ],
  "timestamp": 1789267080032
}
```

> Observação: os itens das listas acima são serializados diretamente a partir dos objetos do **OSHI**. Por isso, o JSON real pode conter mais campos e variar conforme o sistema, a versão da biblioteca e o hardware do computador.

### 3) Batch

Endpoint:

- `POST /v1/telemetry/batch`

Status atual:

- endpoint definido no enum
- Não implementado DTO.

JSON esperado conceitualmente:

```json
{
  "agentId": "PC-001",
  "items": []
}
```

### 4) Events

Endpoint:

- `POST /v1/telemetry/events`

Status atual:

- endpoint definido no enum
- Não implementado DTO

JSON esperado conceitualmente:

```json
{
  "agentId": "PC-001",
  "events": []
}
```

## Observações importantes

- O agente envia dados de forma **assíncrona**.
- O `HeartbeatService` agenda envio a cada **5 segundos**.
- O `SyncService` agenda envio a cada **1 hora**.
- O `HttpClientImpl` faz `POST` com timeout de **5 segundos** para conexão e leitura.

## Build do projeto

O `build.gradle.kts` define:
- aplicação Java
- plugin `shadow` para empacotamento
- dependências de OSHI, Jackson, SLF4J e Lombok
- classe principal: `br.com.oryanps.agent.TelemetryAgentApp`

### Comando de build no Windows

```bat
gradlew.bat shadowJar
```

O artefato gerado fica em:

```text
build/libs/TelemetryAgent.jar
```

## Resumo rápido

- **Projeto:** agente de telemetria para Windows
- **Função:** coletar dados do sistema e enviar para uma API remota
- **Instalação:** pasta da aplicação em `%ProgramFiles%\mTadata Agent`
- **Configuração:** `C:\ProgramData\mTadata Agent\config.json`
- **Rotas principais:** `/v1/telemetry/heartbeat` e `/v1/telemetry/sync`
