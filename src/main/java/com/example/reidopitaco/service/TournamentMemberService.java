package com.example.reidopitaco.service;

import com.example.reidopitaco.dto.response.TournamentMemberResponse;
import com.example.reidopitaco.dto.response.TournamentResponse;
import com.example.reidopitaco.entity.Tournament;
import com.example.reidopitaco.entity.TournamentMember;
import com.example.reidopitaco.entity.User;
import com.example.reidopitaco.enums.TournamentMemberRole;
import com.example.reidopitaco.enums.TournamentMemberStatus;
import com.example.reidopitaco.enums.TournamentStatus;
import com.example.reidopitaco.exception.AlreadyTournamentMemberException;
import com.example.reidopitaco.exception.CannotLeaveAsOwnerException;
import com.example.reidopitaco.exception.NotTournamentOwnerException;
import com.example.reidopitaco.exception.TournamentFullException;
import com.example.reidopitaco.exception.TournamentMemberBannedException;
import com.example.reidopitaco.exception.TournamentNotEditableException;
import com.example.reidopitaco.exception.TournamentNotFoundException;
import com.example.reidopitaco.mapper.TournamentMapper;
import com.example.reidopitaco.mapper.TournamentMemberMapper;
import com.example.reidopitaco.repository.TournamentMemberRepository;
import com.example.reidopitaco.repository.TournamentRepository;
import com.example.reidopitaco.repository.TournamentTeamRepository;
import com.example.reidopitaco.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class TournamentMemberService {

    private final TournamentRepository tournamentRepository;
    private final TournamentMemberRepository memberRepository;
    private final TournamentTeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TournamentMapper tournamentMapper;
    private final TournamentMemberMapper memberMapper;

    public TournamentMemberService(
            TournamentRepository tournamentRepository,
            TournamentMemberRepository memberRepository,
            TournamentTeamRepository teamRepository,
            UserRepository userRepository,
            TournamentMapper tournamentMapper,
            TournamentMemberMapper memberMapper
    ) {
        this.tournamentRepository = tournamentRepository;
        this.memberRepository = memberRepository;
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
        this.tournamentMapper = tournamentMapper;
        this.memberMapper = memberMapper;
    }

    @Transactional
    public TournamentResponse joinByCode(UUID userPublicId, String inviteCode) {
        Tournament tournament = tournamentRepository.findByInviteCodeAndActiveTrue(inviteCode)
                .orElseThrow(TournamentNotFoundException::new);

        if (tournament.getStatus() == TournamentStatus.DRAFT
                || tournament.getStatus() == TournamentStatus.FINISHED) {
            throw new TournamentNotEditableException(
                    tournament.getStatus(),
                    "tournament is not accepting members"
            );
        }

        User user = userRepository.findByPublicId(userPublicId)
                .orElseThrow(TournamentNotFoundException::new);

        TournamentMember member = memberRepository
                .findByTournamentPublicIdAndUserPublicId(tournament.getPublicId(), userPublicId)
                .orElse(null);

        if (member != null) {
            switch (member.getStatus()) {
                case ACTIVE -> throw new AlreadyTournamentMemberException();
                case BANNED -> throw new TournamentMemberBannedException();
                case LEFT -> reactivate(tournament, member);
            }
        } else {
            assertCapacity(tournament);
            memberRepository.save(TournamentMember.builder()
                    .tournament(tournament)
                    .user(user)
                    .role(TournamentMemberRole.PARTICIPANT)
                    .status(TournamentMemberStatus.ACTIVE)
                    .build());
        }

        return buildResponse(tournament);
    }

    @Transactional(readOnly = true)
    public Page<TournamentMemberResponse> list(UUID tournamentPublicId, Pageable pageable) {
        return memberRepository.findAllByTournamentPublicId(tournamentPublicId, pageable)
                .map(memberMapper::toResponse);
    }

    @Transactional
    public void leave(UUID userPublicId, UUID tournamentPublicId) {
        TournamentMember member = memberRepository
                .findByTournamentPublicIdAndUserPublicId(tournamentPublicId, userPublicId)
                .orElseThrow(TournamentNotFoundException::new);

        if (member.getRole() == TournamentMemberRole.OWNER) {
            throw new CannotLeaveAsOwnerException();
        }
        if (member.getStatus() != TournamentMemberStatus.ACTIVE) {
            throw new TournamentNotFoundException();
        }

        member.setStatus(TournamentMemberStatus.LEFT);
        member.setLeftAt(Instant.now());
    }

    @Transactional
    public void ban(UUID ownerPublicId, UUID tournamentPublicId, UUID targetUserPublicId) {
        Tournament tournament = tournamentRepository.findByPublicIdAndActiveTrue(tournamentPublicId)
                .orElseThrow(TournamentNotFoundException::new);
        if (!tournament.getOwner().getPublicId().equals(ownerPublicId)) {
            throw new NotTournamentOwnerException();
        }
        if (targetUserPublicId.equals(ownerPublicId)) {
            throw new CannotLeaveAsOwnerException();
        }

        TournamentMember member = memberRepository
                .findByTournamentPublicIdAndUserPublicId(tournamentPublicId, targetUserPublicId)
                .orElseThrow(TournamentNotFoundException::new);

        Instant now = Instant.now();
        member.setStatus(TournamentMemberStatus.BANNED);
        member.setBannedAt(now);
        if (member.getLeftAt() == null) {
            member.setLeftAt(now);
        }
    }

    private void reactivate(Tournament tournament, TournamentMember member) {
        assertCapacity(tournament);
        member.setStatus(TournamentMemberStatus.ACTIVE);
        member.setLeftAt(null);
    }

    private void assertCapacity(Tournament tournament) {
        if (tournament.getMaxParticipants() == null) {
            return;
        }
        long active = memberRepository.countByTournamentIdAndStatus(
                tournament.getId(),
                TournamentMemberStatus.ACTIVE
        );
        if (active >= tournament.getMaxParticipants()) {
            throw new TournamentFullException("participants");
        }
    }

    private TournamentResponse buildResponse(Tournament tournament) {
        long memberCount = memberRepository.countByTournamentIdAndStatus(
                tournament.getId(),
                TournamentMemberStatus.ACTIVE
        );
        long teamCount = teamRepository.countByTournamentId(tournament.getId());
        return tournamentMapper.toResponse(tournament, memberCount, teamCount);
    }
}
