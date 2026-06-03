package com.example.reidopitaco.mapper;

import com.example.reidopitaco.dto.response.TournamentMemberResponse;
import com.example.reidopitaco.entity.TournamentMember;
import com.example.reidopitaco.service.AvatarService;
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
