package com.example.reidopitaco.enums;

/**
 * Classifica um time entre clube e seleção nacional. Usado principalmente para os times padrão
 * do sistema (ver {@code Team.system}), permitindo ao front separar "clubes do sistema" de
 * "seleções do sistema". Times criados por usuário são {@code CLUB} por padrão.
 */
public enum TeamType {
    CLUB,
    NATIONAL_TEAM
}
