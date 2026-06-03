package com.example.reidopitaco.dto.response;

/**
 * Distribuição agregada dos palpites de uma partida (quantos apostam em mandante/empate/visitante),
 * sem expor palpites individuais. Os percentuais são arredondados no servidor e somam 100
 * (exceto quando não há votos, caso em que tudo é 0).
 */
public record PredictionStatsResponse(
        long totalVotes,
        long homeWin,
        long draw,
        long awayWin,
        int homeWinPct,
        int drawPct,
        int awayWinPct
) {
}
