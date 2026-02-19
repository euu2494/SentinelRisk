package com.sentinelrisk.sentinelrisk.domain.risk;

import com.sentinelrisk.sentinelrisk.domain.risk.dto.RiskRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class RiskMapper {

    public Risk toEntity(RiskRequestDTO dto) {
        if (dto == null) return null;

        Risk risk = new Risk();
        risk.setTitle(dto.title());
        risk.setDescription(dto.description());
        risk.setOwner(dto.owner());

        return risk;
    }
}