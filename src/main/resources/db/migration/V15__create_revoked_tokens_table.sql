-- Denylist de refresh tokens revogados (logout e rotação no refresh).
-- Stateless continua valendo para access tokens; só refresh tokens são revogáveis aqui.
CREATE TABLE revoked_tokens (
    id BIGSERIAL PRIMARY KEY,
    jti UUID NOT NULL UNIQUE,
    user_public_id UUID,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    revoked_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- expires_at permite uma limpeza futura (job) das entradas que já passaram do exp do token.
CREATE INDEX idx_revoked_tokens_expires_at ON revoked_tokens (expires_at);
