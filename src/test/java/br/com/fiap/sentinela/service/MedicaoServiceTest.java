package br.com.fiap.sentinela.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import br.com.fiap.sentinela.model.StatusMedicao;

class MedicaoServiceTest {

    @Test
    void calculaStatusPelosLimites() {
        assertEquals(StatusMedicao.NORMAL, MedicaoService.calcularStatus(50.0));
        assertEquals(StatusMedicao.NORMAL, MedicaoService.calcularStatus(80.0));
        assertEquals(StatusMedicao.ALERTA, MedicaoService.calcularStatus(80.1));
        assertEquals(StatusMedicao.ALERTA, MedicaoService.calcularStatus(100.0));
        assertEquals(StatusMedicao.CRITICO, MedicaoService.calcularStatus(100.1));
    }

    @Test
    void valorSimuladoFicaNaFaixaEsperada() {
        for (int i = 0; i < 1_000; i++) {
            double valor = MedicaoService.gerarValorSimulado();
            assertTrue(valor >= 50.0 && valor <= 115.0, "valor fora da faixa: " + valor);
        }
    }
}
