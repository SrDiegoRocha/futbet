package com.example.reidopitaco.service;

import com.example.reidopitaco.config.AvatarProperties;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Monta a URL do avatar (DiceBear) a partir de uma seed — no Rei do Pitaco, o nome do usuário.
 * Determinístico: mesma seed → mesmo avatar; nome diferente → avatar diferente. O backend só
 * monta a string da URL; quem busca a imagem é o navegador do cliente (sem custo/limite no backend).
 */
@Service
public class AvatarService {

    private final AvatarProperties properties;

    public AvatarService(AvatarProperties properties) {
        this.properties = properties;
    }

    public String avatarUrlFor(String seed) {
        String value = seed == null ? "" : seed.trim();
        // Espaço como %20 (e não '+') para uma seed estável e inequívoca.
        String encoded = URLEncoder.encode(value, StandardCharsets.UTF_8).replace("+", "%20");
        return properties.baseUrl() + "/" + properties.version() + "/" + properties.style()
                + "/svg?seed=" + encoded;
    }
}
