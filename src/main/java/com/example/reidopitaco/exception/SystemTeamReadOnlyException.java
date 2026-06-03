package com.example.reidopitaco.exception;

import org.springframework.http.HttpStatus;

/**
 * Lançada ao tentar editar ou deletar um time padrão do sistema (`Team.system = true`).
 * Esses times são compartilhados e somente leitura para os usuários.
 */
public class SystemTeamReadOnlyException extends BusinessException {

    public SystemTeamReadOnlyException() {
        super("System teams cannot be modified or deleted", HttpStatus.FORBIDDEN);
    }
}
