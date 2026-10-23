package br.com.fiap.sentinela.config;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.com.fiap.sentinela.model.Sensor;
import br.com.fiap.sentinela.repository.SensorRepository;
import br.com.fiap.sentinela.service.MedicaoService;

/**
 * Carga inicial: na primeira execução (banco vazio) cria os sensores da linha
 * e algumas medições, para o app já abrir com dados.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final SensorRepository sensorRepository;
    private final MedicaoService medicaoService;

    public DataLoader(SensorRepository sensorRepository, MedicaoService medicaoService) {
        this.sensorRepository = sensorRepository;
        this.medicaoService = medicaoService;
    }

    @Override
    public void run(String... args) {
        if (sensorRepository.count() > 0) {
            return;
        }

        List<Sensor> sensores = sensorRepository.saveAll(List.of(
                new Sensor("Motor da Esteira 01", "Temperatura", "°C"),
                new Sensor("Cabine de Prensagem", "Ruído", "dB"),
                new Sensor("Motor da Bomba 02", "Carga", "%")));

        Sensor temperatura = sensores.get(0);
        Sensor ruido = sensores.get(1);
        Sensor carga = sensores.get(2);

        LocalDateTime agora = LocalDateTime.now();
        medicaoService.salvar(temperatura, 64.2, agora.minusMinutes(50));
        medicaoService.salvar(ruido, 78.5, agora.minusMinutes(42));
        medicaoService.salvar(carga, 71.0, agora.minusMinutes(35));
        medicaoService.salvar(temperatura, 86.7, agora.minusMinutes(24));
        medicaoService.salvar(ruido, 103.4, agora.minusMinutes(12));
        medicaoService.salvar(carga, 58.9, agora.minusMinutes(5));
    }
}
