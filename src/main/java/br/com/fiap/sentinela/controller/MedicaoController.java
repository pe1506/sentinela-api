package br.com.fiap.sentinela.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.sentinela.dto.NovaMedicaoRequest;
import br.com.fiap.sentinela.dto.SimulacaoRequest;
import br.com.fiap.sentinela.model.Medicao;
import br.com.fiap.sentinela.service.MedicaoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/medicoes")
@CrossOrigin(origins = "*") // libera o app Expo (outra porta/origem)
public class MedicaoController {

    private final MedicaoService medicaoService;

    public MedicaoController(MedicaoService medicaoService) {
        this.medicaoService = medicaoService;
    }

    /** GET /api/medicoes[?sensorId=1] — medições da mais recente para a mais antiga. */
    @GetMapping
    public List<Medicao> listar(@RequestParam(required = false) Long sensorId) {
        return medicaoService.listar(sensorId);
    }

    /** POST /api/medicoes — registra uma coleta com valor informado. */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Medicao registrar(@RequestBody @Valid NovaMedicaoRequest request) {
        return medicaoService.registrar(request.sensorId(), request.valor());
    }

    /** POST /api/medicoes/simular — gera e grava uma coleta simulada (usado pelo botão do app). */
    @PostMapping("/simular")
    @ResponseStatus(HttpStatus.CREATED)
    public Medicao simular(@RequestBody(required = false) SimulacaoRequest request) {
        Long sensorId = request != null ? request.sensorId() : null;
        return medicaoService.simular(sensorId);
    }
}
