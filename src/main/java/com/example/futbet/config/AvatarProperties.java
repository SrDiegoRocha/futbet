package com.example.futbet.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuração do gerador de avatares (DiceBear). O avatar é determinístico por seed (o nome do
 * usuário). Ajustável via env: {@code AVATAR_BASE_URL}, {@code AVATAR_VERSION}, {@code AVATAR_STYLE}.
 */
@ConfigurationProperties(prefix = "futbet.avatar")
public record AvatarProperties(
        String baseUrl,
        String version,
        String style
) {
    public AvatarProperties {
        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = "https://api.dicebear.com";
        }
        if (version == null || version.isBlank()) {
            version = "9.x";
        }
        if (style == null || style.isBlank()) {
            style = "fun-emoji";
        }
    }
}
