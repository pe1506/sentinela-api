package br.com.fiap.sentinela.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.sentinela.model.Medicao;

public interface MedicaoRepository extends JpaRepository<Medicao, Long> {

    /** Todas as medições, da mais recente para a mais antiga. */
    List<Medicao> findAllByOrderByDataDescIdDesc();

    /** Medições de um sensor, da mais recente para a mais antiga. */
    List<Medicao> findBySensorIdOrderByDataDescIdDesc(Long sensorId);
}
