package br.com.oryanps.agent.dto;

import br.com.oryanps.agent.core.interfaces.IData;
import lombok.Getter;

@Getter
public class BufferData implements IData {



    @Override
    public String getAgentId() {
        return "";
    }
}
