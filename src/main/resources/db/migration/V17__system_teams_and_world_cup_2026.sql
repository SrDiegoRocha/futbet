-- Times padrão do sistema (seleções/clubes pré-cadastrados, não editáveis pelo usuário).

-- Garante gen_random_uuid() (nativo no Postgres 13+; em versões antigas vem do pgcrypto).
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Passo 1: estrutura.

-- Times do sistema não têm dono.
ALTER TABLE teams
    ALTER COLUMN owner_id DROP NOT NULL;

-- Flag de "time do sistema" (não editável/deletável) e classificação clube x seleção.
ALTER TABLE teams
    ADD COLUMN is_system   BOOLEAN     NOT NULL DEFAULT FALSE,
    ADD COLUMN team_type   VARCHAR(20) NOT NULL DEFAULT 'CLUB',
    ADD COLUMN country_code VARCHAR(10);

-- Nome único entre os times do sistema ativos (os de usuário continuam com o índice por dono).
CREATE UNIQUE INDEX uq_teams_system_name_active
    ON teams (LOWER(name))
    WHERE is_system = TRUE AND active = TRUE;

-- Passo 2: seed das 48 seleções da Copa do Mundo 2026.
-- name = nome em português; short_name = sigla oficial FIFA (3 letras);
-- country_code = código de bandeira do flagicons.lipis.dev (ISO alpha-2; subdivisões gb-eng/gb-sct).
-- Cores aproximam as cores oficiais de manto/bandeira — ajustáveis depois.
INSERT INTO teams
    (public_id, name, short_name, primary_color, secondary_color, country_code, is_system, team_type, active, created_at, updated_at)
VALUES
    -- AFC (9)
    (gen_random_uuid(), 'Austrália',        'AUS', '#FFCD00', '#00843D', 'au', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Irã',              'IRN', '#C8102E', '#239F40', 'ir', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Iraque',           'IRQ', '#007A3D', '#FFFFFF', 'iq', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Japão',            'JPN', '#000066', '#FFFFFF', 'jp', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Jordânia',         'JOR', '#007A3D', '#CE1126', 'jo', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Catar',            'QAT', '#8A1538', '#FFFFFF', 'qa', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Arábia Saudita',   'KSA', '#006C35', '#FFFFFF', 'sa', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Coreia do Sul',    'KOR', '#C8102E', '#0A2463', 'kr', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Uzbequistão',      'UZB', '#1EB53A', '#0099B5', 'uz', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    -- CAF (10)
    (gen_random_uuid(), 'Argélia',          'ALG', '#006233', '#FFFFFF', 'dz', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Cabo Verde',       'CPV', '#003893', '#CF2027', 'cv', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'República Democrática do Congo', 'COD', '#007FFF', '#F7D518', 'cd', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Egito',            'EGY', '#CE1126', '#000000', 'eg', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Gana',             'GHA', '#CE1126', '#FCD116', 'gh', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Costa do Marfim',  'CIV', '#FF8200', '#009E60', 'ci', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Marrocos',         'MAR', '#C1272D', '#006233', 'ma', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Senegal',          'SEN', '#00853F', '#FDEF42', 'sn', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'África do Sul',    'RSA', '#007A4D', '#FFB915', 'za', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Tunísia',          'TUN', '#E70013', '#FFFFFF', 'tn', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    -- CONCACAF (6)
    (gen_random_uuid(), 'Canadá',           'CAN', '#FF0000', '#FFFFFF', 'ca', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Curaçao',          'CUW', '#002B7F', '#F9E814', 'cw', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Haiti',            'HAI', '#00209F', '#D21034', 'ht', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'México',           'MEX', '#006847', '#CE1126', 'mx', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Panamá',           'PAN', '#DA121A', '#005293', 'pa', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Estados Unidos',   'USA', '#0A3161', '#B31942', 'us', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    -- CONMEBOL (6)
    (gen_random_uuid(), 'Argentina',        'ARG', '#6CACE4', '#FFFFFF', 'ar', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Brasil',           'BRA', '#FFDF00', '#009739', 'br', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Colômbia',         'COL', '#FCD116', '#003893', 'co', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Equador',          'ECU', '#FFD100', '#0072CE', 'ec', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Paraguai',         'PAR', '#D52B1E', '#0038A8', 'py', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Uruguai',          'URU', '#6CACE4', '#001489', 'uy', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    -- OFC (1)
    (gen_random_uuid(), 'Nova Zelândia',    'NZL', '#000000', '#FFFFFF', 'nz', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    -- UEFA (16)
    (gen_random_uuid(), 'Áustria',          'AUT', '#ED2939', '#FFFFFF', 'at', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Bélgica',          'BEL', '#E30613', '#000000', 'be', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Bósnia e Herzegovina', 'BIH', '#002395', '#FFCD00', 'ba', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Croácia',          'CRO', '#FF0000', '#FFFFFF', 'hr', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'República Tcheca', 'CZE', '#11457E', '#D7141A', 'cz', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Inglaterra',       'ENG', '#001489', '#CE1124', 'gb-eng', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'França',           'FRA', '#002395', '#ED2939', 'fr', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Alemanha',         'GER', '#000000', '#DD0000', 'de', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Países Baixos',    'NED', '#FF6200', '#21468B', 'nl', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Noruega',          'NOR', '#BA0C2F', '#00205B', 'no', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Portugal',         'POR', '#006600', '#FF0000', 'pt', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Escócia',          'SCO', '#0065BF', '#FFFFFF', 'gb-sct', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Espanha',          'ESP', '#C60B1E', '#FFC400', 'es', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Suécia',           'SWE', '#006AA7', '#FECC00', 'se', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Suíça',            'SUI', '#FF0000', '#FFFFFF', 'ch', TRUE, 'NATIONAL_TEAM', TRUE, now(), now()),
    (gen_random_uuid(), 'Turquia',          'TUR', '#E30A17', '#FFFFFF', 'tr', TRUE, 'NATIONAL_TEAM', TRUE, now(), now());
