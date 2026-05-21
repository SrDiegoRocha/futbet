ALTER TABLE tournament_settings
    DROP COLUMN match_leg_mode,
    DROP COLUMN match_generation_mode,
    DROP COLUMN qualifiers_per_group,
    DROP COLUMN groups_count;

ALTER TABLE tournament_phases
    ADD COLUMN match_generation_mode VARCHAR(15) NOT NULL DEFAULT 'AUTOMATIC',
    ADD COLUMN qualifiers_per_group INTEGER;

ALTER TABLE tournament_phases
    ALTER COLUMN match_generation_mode DROP DEFAULT;
