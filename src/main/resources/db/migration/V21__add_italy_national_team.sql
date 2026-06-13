-- Adiciona a seleção da Itália aos times padrão do sistema (seleções).
-- Mesmo formato do seed da Copa 2026 (V17): nome em português, sigla FIFA,
-- country_code do flagicons.lipis.dev (ISO alpha-2) e cores aproximadas das oficiais.
INSERT INTO teams
    (public_id, name, short_name, primary_color, secondary_color, country_code, is_system, team_type, active, created_at, updated_at)
VALUES
    (gen_random_uuid(), 'Itália', 'ITA', '#0066CC', '#FFFFFF', 'it', TRUE, 'NATIONAL_TEAM', TRUE, now(), now());
