package com.example.reidopitaco.enums;

/**
 * Escopo de listagem de times em {@code GET /api/teams}:
 * <ul>
 *   <li>{@code MINE} — só os times do próprio usuário (padrão).</li>
 *   <li>{@code SYSTEM} — só os times padrão do sistema (seleções/clubes).</li>
 *   <li>{@code ALL} — os do usuário + os do sistema.</li>
 * </ul>
 */
public enum TeamScope {
    MINE,
    SYSTEM,
    ALL
}
