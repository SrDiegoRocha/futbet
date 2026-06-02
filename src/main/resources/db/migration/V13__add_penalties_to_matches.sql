ALTER TABLE tournament_matches
    ADD COLUMN home_penalties INTEGER,
    ADD COLUMN away_penalties INTEGER;

-- Pênaltis vêm em par e nunca empatam (a disputa sempre tem um vencedor).
ALTER TABLE tournament_matches
    ADD CONSTRAINT chk_match_penalties CHECK (
        (home_penalties IS NULL AND away_penalties IS NULL)
        OR (
            home_penalties IS NOT NULL AND away_penalties IS NOT NULL
            AND home_penalties >= 0 AND away_penalties >= 0
            AND home_penalties <> away_penalties
        )
    );
