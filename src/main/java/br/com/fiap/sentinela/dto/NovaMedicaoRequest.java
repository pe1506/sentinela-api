package br.com.fiap.sentinela.dto;

import jakarta.validation.constraints.NotNull;

/** Corpo do POST /api/medicoes (registro manual de uma coleta). */
public record NovaMedicaoRequest(
        @NotNull(message = "sensorId é obrigatório") Long sensorId,
        @NotNull(message = "valor é obrigatório") Double valor) {
}
