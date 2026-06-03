package com.example.reidopitaco.enums;

/**
 * Natureza de uma partida dentro de uma fase. Relevante em mata-mata para distinguir a disputa
 * de 3º lugar dos demais confrontos. Persistido (não inferido) para tornar o chaveamento robusto.
 */
public enum MatchType {
    REGULAR,
    THIRD_PLACE
}
