package com.example.reidopitaco.service;

import com.example.reidopitaco.entity.Tournament;
import com.example.reidopitaco.enums.TournamentMemberStatus;
import com.example.reidopitaco.enums.TournamentPrivacy;
import com.example.reidopitaco.enums.TournamentStatus;
import com.example.reidopitaco.exception.TournamentNotFoundException;
import com.example.reidopitaco.repository.TournamentMemberRepository;
import com.example.reidopitaco.repository.TournamentRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Centraliza a regra de quem pode <b>ver</b> um torneio (e seus dados derivados: ranking,
 * standings, bracket, partidas). Fonte única de verdade para não vazar conteúdo de torneios
 * PRIVATE entre usuários.
 *
 * <p>Pode ver: o owner, um membro <b>ACTIVE</b>, ou qualquer autenticado se o torneio for
 * PUBLIC e não estiver em DRAFT. Quem deixou o torneio (LEFT) ou foi banido (BANNED) perde o
 * acesso a torneios PRIVATE — coerente com a regra "participantes expulsos perdem acesso".
 * Recursos inacessíveis retornam 404 (não diferenciamos de inexistente, para não vazar existência).
 */
@Component
public class TournamentAccessGuard {

    private final TournamentRepository tournamentRepository;
    private final TournamentMemberRepository memberRepository;

    public TournamentAccessGuard(
            TournamentRepository tournamentRepository,
            TournamentMemberRepository memberRepository
    ) {
        this.tournamentRepository = tournamentRepository;
        this.memberRepository = memberRepository;
    }

    public Tournament requireViewable(UUID requesterPublicId, UUID tournamentPublicId) {
        Tournament tournament = tournamentRepository.findByPublicIdAndActiveTrue(tournamentPublicId)
                .orElseThrow(TournamentNotFoundException::new);

        boolean isOwner = tournament.getOwner().getPublicId().equals(requesterPublicId);
        boolean isActiveMember = memberRepository
                .findByTournamentPublicIdAndUserPublicId(tournamentPublicId, requesterPublicId)
                .filter(m -> m.getStatus() == TournamentMemberStatus.ACTIVE)
                .isPresent();
        boolean isPublic = tournament.getPrivacy() == TournamentPrivacy.PUBLIC
                && tournament.getStatus() != TournamentStatus.DRAFT;

        if (!isOwner && !isActiveMember && !isPublic) {
            throw new TournamentNotFoundException();
        }
        return tournament;
    }
}
