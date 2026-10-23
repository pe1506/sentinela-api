package br.com.fiap.sentinela.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.sentinela.model.Sensor;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
}
