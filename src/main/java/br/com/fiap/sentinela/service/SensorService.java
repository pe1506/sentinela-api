package br.com.fiap.sentinela.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.fiap.sentinela.exception.RecursoNaoEncontradoException;
import br.com.fiap.sentinela.model.Sensor;
import br.com.fiap.sentinela.repository.SensorRepository;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public List<Sensor> listar() {
        return sensorRepository.findAll();
    }

    public Sensor buscarPorId(Long id) {
        return sensorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sensor " + id + " não encontrado"));
    }
}
