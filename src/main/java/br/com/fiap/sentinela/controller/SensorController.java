package br.com.fiap.sentinela.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.sentinela.model.Sensor;
import br.com.fiap.sentinela.service.SensorService;

@RestController
@RequestMapping("/api/sensores")
@CrossOrigin(origins = "*") // libera o app Expo (outra porta/origem)
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    /** GET /api/sensores — lista os sensores cadastrados. */
    @GetMapping
    public List<Sensor> listar() {
        return sensorService.listar();
    }

    /** GET /api/sensores/{id} — detalhe de um sensor. */
    @GetMapping("/{id}")
    public Sensor buscar(@PathVariable Long id) {
        return sensorService.buscarPorId(id);
    }
}
