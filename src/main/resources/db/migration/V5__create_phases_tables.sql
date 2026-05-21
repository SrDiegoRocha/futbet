ALTER TABLE tournament_settings
    DROP COLUMN plays_inside_group_only;

CREATE TABLE tournament_phases (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL UNIQUE,
    tournament_id BIGINT NOT NULL REFERENCES tournaments (id) ON DELETE CASCADE,
    name VARCHAR(60) NOT NULL,
    position INTEGER NOT NULL,
    phase_type VARCHAR(15) NOT NULL,
    match_leg_mode VARCHAR(15) NOT NULL,
    plays_inside_group_only BOOLEAN,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    UNIQUE (tournament_id, position)
);

CREATE INDEX idx_phases_tournament_id ON tournament_phases (tournament_id);
CREATE INDEX idx_phases_public_id ON tournament_phases (public_id);

CREATE TABLE phase_groups (
    id BIGSERIAL PRIMARY KEY,
    public_id UUID NOT NULL UNIQUE,
    phase_id BIGINT NOT NULL REFERENCES tournament_phases (id) ON DELETE CASCADE,
    name VARCHAR(40) NOT NULL,
    position INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    UNIQUE (phase_id, position)
);

CREATE INDEX idx_phase_groups_phase_id ON phase_groups (phase_id);
CREATE UNIQUE INDEX uq_phase_groups_phase_lower_name ON phase_groups (phase_id, LOWER(name));

CREATE TABLE phase_teams (
    id BIGSERIAL PRIMARY KEY,
    phase_id BIGINT NOT NULL REFERENCES tournament_phases (id) ON DELETE CASCADE,
    team_id BIGINT NOT NULL REFERENCES teams (id),
    group_id BIGINT REFERENCES phase_groups (id) ON DELETE SET NULL,
    added_at TIMESTAMP WITH TIME ZONE NOT NULL,
    UNIQUE (phase_id, team_id)
);

CREATE INDEX idx_phase_teams_phase_id ON phase_teams (phase_id);
CREATE INDEX idx_phase_teams_team_id ON phase_teams (team_id);
CREATE INDEX idx_phase_teams_group_id ON phase_teams (group_id);
