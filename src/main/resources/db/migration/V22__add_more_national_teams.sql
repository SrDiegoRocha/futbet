-- Adiciona mais seleções aos times padrão do sistema.
-- Mesmo formato do seed da Copa 2026 (V17): nome em português, sigla FIFA,
-- country_code do flagicons.lipis.dev (ISO alpha-2; subdivisões gb-* p/ Reino Unido)
-- e cores aproximadas das oficiais (ajustáveis depois).
--
-- Idempotente: cada linha só é inserida se ainda não houver um time do sistema
-- ativo com o mesmo nome (case-insensitive). Nomes já existentes são pulados.
INSERT INTO teams
    (public_id, name, short_name, primary_color, secondary_color, country_code, is_system, team_type, active, created_at, updated_at)
SELECT
    gen_random_uuid(), v.name, v.short_name, v.primary_color, v.secondary_color, v.country_code,
    TRUE, 'NATIONAL_TEAM', TRUE, now(), now()
FROM (VALUES
    ('Dinamarca',               'DEN', '#C60C30', '#FFFFFF', 'dk'),
    ('Nigéria',                 'NGA', '#008751', '#FFFFFF', 'ng'),
    ('Ucrânia',                 'UKR', '#005BBB', '#FFD500', 'ua'),
    ('Rússia',                  'RUS', '#FFFFFF', '#D52B1E', 'ru'),
    ('Polônia',                 'POL', '#DC143C', '#FFFFFF', 'pl'),
    ('País de Gales',           'WAL', '#C8102E', '#00B140', 'gb-wls'),
    ('Hungria',                 'HUN', '#CD2A3E', '#436F4D', 'hu'),
    ('Sérvia',                  'SRB', '#C6363C', '#0C4076', 'rs'),
    ('Camarões',                'CMR', '#007A5E', '#CE1126', 'cm'),
    ('Grécia',                  'GRE', '#0D5EAF', '#FFFFFF', 'gr'),
    ('Eslováquia',              'SVK', '#0B4EA2', '#EE1C25', 'sk'),
    ('Peru',                    'PER', '#D91023', '#FFFFFF', 'pe'),
    ('Costa Rica',              'CRC', '#002B7F', '#CE1126', 'cr'),
    ('Romênia',                 'ROU', '#002B7F', '#FCD116', 'ro'),
    ('Chile',                   'CHI', '#0039A6', '#D52B1E', 'cl'),
    ('Eslovênia',               'SVN', '#005DA4', '#ED1C24', 'si'),
    ('República da Irlanda',    'IRL', '#169B62', '#FF883E', 'ie'),
    ('Honduras',                'HON', '#0073CF', '#FFFFFF', 'hn'),
    ('Albânia',                 'ALB', '#E41E20', '#000000', 'al'),
    ('Macedônia do Norte',      'MKD', '#D20000', '#FFE600', 'mk'),
    ('Jamaica',                 'JAM', '#009B3A', '#FED100', 'jm'),
    ('Geórgia',                 'GEO', '#FF0000', '#FFFFFF', 'ge'),
    ('Islândia',                'ISL', '#003897', '#D72828', 'is'),
    ('Finlândia',               'FIN', '#003580', '#FFFFFF', 'fi'),
    ('Israel',                  'ISR', '#0038B8', '#FFFFFF', 'il'),
    ('Bolívia',                 'BOL', '#007934', '#FFE000', 'bo'),
    ('Angola',                  'ANG', '#CE1126', '#000000', 'ao'),
    ('Zâmbia',                  'ZAM', '#198A00', '#EF7D00', 'zm'),
    ('Guatemala',               'GUA', '#4997D0', '#FFFFFF', 'gt'),
    ('Luxemburgo',              'LUX', '#00A1DE', '#ED2939', 'lu'),
    ('El Salvador',             'SLV', '#0047AB', '#FFFFFF', 'sv'),
    ('Trinidad e Tobago',       'TRI', '#DA1A35', '#000000', 'tt')
) AS v(name, short_name, primary_color, secondary_color, country_code)
WHERE NOT EXISTS (
    SELECT 1 FROM teams t
    WHERE t.is_system = TRUE AND t.active = TRUE
      AND LOWER(t.name) = LOWER(v.name)
);
