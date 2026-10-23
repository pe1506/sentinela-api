package br.com.fiap.sentinela.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.sentinela.exception.RecursoNaoEncontradoException;
import br.com.fiap.sentinela.model.Medicao;
import br.com.fiap.sentinela.model.Sensor;
import br.com.fiap.sentinela.model.StatusMedicao;
import br.com.fiap.sentinela.repository.MedicaoRepository;
import br.com.fiap.sentinela.repository.SensorRepository;

/**
 * Regra de negócio do monitoramento: cálculo do status e simulação de coleta.
 * Fica no servidor; o app só exibe o que a API devolve.
 */
@Service
public class MedicaoService {

    /** Acima deste valor a medição entra em ALERTA. */
    public static final double LIMITE_ALERTA = 80.0;
    /** Acima deste valor a medição é CRÍTICA. */
    public static final double LIMITE_CRITICO = 100.0;

    private final MedicaoRepository medicaoRepository;
    private final SensorRepository sensorRepository;
    private final SensorService sensorService;

    public MedicaoService(MedicaoRepository medicaoRepository,
                          SensorRepository sensorRepository,
                          SensorService sensorService) {
        this.medicaoRepository = medicaoRepository;
        this.sensorRepository = sensorRepository;
        this.sensorService = sensorService;
    }

    /** Mesma regra do calcularStatus do app (Sprint 2). */
    public static StatusMedicao calcularStatus(double valor) {
        if (valor > LIMITE_CRITICO) return StatusMedicao.CRITICO;
        if (valor > LIMITE_ALERTA) return StatusMedicao.ALERTA;
        return StatusMedicao.NORMAL;
    }

    public List<Medicao> listar(Long sensorId) {
        if (sensorId == null) {
            return medicaoRepository.findAllByOrderByDataDescIdDesc();
        }
        sensorService.buscarPorId(sensorId); // 404 se o sensor não existir
        return medicaoRepository.findBySensorIdOrderByDataDescIdDesc(sensorId);
    }

    /** Registra uma coleta com valor informado (POST /api/medicoes). */
    @Transactional
    public Medicao registrar(Long sensorId, double valor) {
        Sensor sensor = sensorService.buscarPorId(sensorId);
        return salvar(sensor, valor, LocalDateTime.now());
    }

    /**
     * Simula uma coleta (POST /api/medicoes/simular): gera um valor para o sensor
     * escolhido (ou para um sensor sorteado) e grava no banco.
     */
    @Transactional
    public Medicao simular(Long sensorId) {
        Sensor sensor = sensorId != null ? sensorService.buscarPorId(sensorId) : sortearSensor();
        return salvar(sensor, gerarValorSimulado(), LocalDateTime.now());
    }

    /** Usado também pela carga inicial de dados. */
    @Transactional
    public Medicao salvar(Sensor sensor, double valor, LocalDateTime data) {
        Medicao medicao = new Medicao(sensor, valor, data.withNano(0), calcularStatus(valor));
        return medicaoRepository.save(medicao);
    }

    /**
     * Valor entre 50 e 115 com uma casa decimal.
     * ~60% NORMAL, ~25% ALERTA, ~15% CRÍTICO, para todos os status aparecerem na demonstração.
     */
    static double gerarValorSimulado() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        double sorteio = random.nextDouble();
        double valor;
        if (sorteio < 0.60) {
            valor = 50.0 + random.nextDouble() * 30.0;    // 50.0 – 80.0  -> normal
        } else if (sorteio < 0.85) {
            valor = 80.1 + random.nextDouble() * 19.9;    // 80.1 – 100.0 -> alerta
        } else {
            valor = 100.1 + random.nextDouble() * 14.9;   // 100.1 – 115.0 -> crítico
        }
        return Math.round(valor * 10.0) / 10.0;
    }

    private Sensor sortearSensor() {
        List<Sensor> sensores = sensorRepository.findAll();
        if (sensores.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum sensor cadastrado");
        }
        return sensores.get(ThreadLocalRandom.current().nextInt(sensores.size()));
    }
}
