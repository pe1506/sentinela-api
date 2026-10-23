package br.com.fiap.sentinela.dto;

/**
 * Corpo (opcional) do POST /api/medicoes/simular.
 * Se sensorId vier nulo, o servidor sorteia um sensor.
 */
public record SimulacaoRequest(Long sensorId) {
}
