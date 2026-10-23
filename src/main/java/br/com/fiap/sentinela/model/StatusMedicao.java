package br.com.fiap.sentinela.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Status de uma medição. No JSON sai em minúsculas ("normal", "alerta", "critico"),
 * igual ao tipo usado no app.
 */
public enum StatusMedicao {
    NORMAL,
    ALERTA,
    CRITICO;

    @JsonValue
    public String toJson() {
        return name().toLowerCase();
    }
}
