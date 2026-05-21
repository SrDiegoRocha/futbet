package com.example.futbet.mapper;

import com.example.futbet.dto.response.PhaseGroupResponse;
import com.example.futbet.entity.PhaseGroup;
import org.springframework.stereotype.Component;

@Component
public class PhaseGroupMapper {

    public PhaseGroupResponse toResponse(PhaseGroup group, long teamCount) {
        return new PhaseGroupResponse(
                group.getPublicId(),
                group.getName(),
                group.getPosition(),
                teamCount,
                group.getCreatedAt(),
                group.getUpdatedAt()
        );
    }
}
