-- Encolhe invite_code de 8 para 6 caracteres.
-- Códigos existentes são truncados para os primeiros 6 caracteres via USING.
-- Caso a truncagem gere colisão na constraint UNIQUE, a migration falha e o
-- código precisa ser regenerado manualmente (cenário aceitável dado o estágio do projeto).
ALTER TABLE tournaments
    ALTER COLUMN invite_code TYPE VARCHAR(6) USING SUBSTRING(invite_code FROM 1 FOR 6);
