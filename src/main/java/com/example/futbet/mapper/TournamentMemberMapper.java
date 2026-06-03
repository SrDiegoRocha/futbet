package com.example.futbet.mapper;

import com.example.futbet.dto.response.TournamentMemberResponse;
import com.example.futbet.entity.TournamentMember;
import com.example.futbet.service.AvatarService;
import org.springframework.stereotype.Component;

@Component
public class TournamentMemberMapper {

    private final AvatarService avatarService;

    public TournamentMemberMapper(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    public TournamentMemberResponse toResponse(TournamentMember member) {
        return new TournamentMemberResponse(
                member.getUser().getPublicId(),
                member.getUser().getName(),
                avatarService.avatarUrlFor(member.getUser().getName()),
                member.getRole(),
                member.getStatus(),
                member.getJoinedAt(),
                member.getLeftAt(),
                member.getBannedAt()
        );
    }
}
