# Sentinela — Monitoramento de Sensores (API)

API Spring Boot do **Sentinela**, sistema que monitora os sensores de uma linha de produção industrial. A API guarda os sensores e as medições, calcula o status de cada leitura (**NORMAL**, **ALERTA** ou **CRÍTICO**) e simula novas coletas. O app React Native (Expo) consome esta API.

| Sensor | Tipo | Unidade |
|---|---|---|
| Motor da Esteira 01 | Temperatura | °C |
| Cabine de Prensagem | Ruído | dB |
| Motor da Bomba 02 | Carga | % |

## Integrantes

| Nome | RM |
|---|---|
| NOME COMPLETO | RM00000 |
| NOME COMPLETO | RM00000 |
| NOME COMPLETO | RM00000 |

## Repositórios e vídeo

- Backend (Spring Boot): https://github.com/SEU-USUARIO/sentinela-api
- Frontend (React Native / Expo): https://github.com/SEU-USUARIO/sentinela-app
- Vídeo de demonstração (YouTube, não listado): https://youtu.be/SEU-VIDEO

## Stack

Java 17, Spring Boot 3 (Web, Data JPA, Validation), banco H2 em arquivo. App: React Native / Expo / TypeScript.

## Como rodar

Pré-requisito: **Java 17 ou superior** (`java -version`). Não precisa instalar Maven nem banco.

```bash
./mvnw spring-boot:run          # Windows (cmd/PowerShell): mvnw spring-boot:run
```

A API sobe em **http://localhost:8080**. Na primeira execução o `mvnw` baixa o Maven e as dependências, o que leva alguns minutos. Também dá para rodar pela IDE, executando `SentinelaApiApplication`.

Teste rápido (deve devolver JSON):

```bash
curl http://localhost:8080/api/medicoes
```

Na primeira execução, com o banco vazio, a API cria os 3 sensores e 6 medições de exemplo (`config/DataLoader`).

## Estrutura

```
src/main/java/br/com/fiap/sentinela/
  controller/   SensorController, MedicaoController   (@CrossOrigin para o app)
  service/      SensorService, MedicaoService         (regra de status e simulação)
  repository/   SensorRepository, MedicaoRepository   (Spring Data JPA)
  model/        Sensor, Medicao, StatusMedicao
  dto/          NovaMedicaoRequest, SimulacaoRequest
  exception/    erros em JSON (404, 400)
  config/       DataLoader (carga inicial)
```

## Modelo

```json
{
  "id": 7,
  "sensor": { "id": 1, "nome": "Motor da Esteira 01", "tipo": "Temperatura", "unidade": "°C" },
  "valor": 86.7,
  "data": "2026-10-22T21:40:12",
  "status": "alerta"
}
```

## Regra de status

Fica no `MedicaoService` e é aplicada sempre que uma medição é gravada:

```java
public static StatusMedicao calcularStatus(double valor) {
    if (valor > 100) return StatusMedicao.CRITICO;
    if (valor > 80) return StatusMedicao.ALERTA;
    return StatusMedicao.NORMAL;
}
```

## Endpoints

| Método | Rota | Descrição | Usado pelo app |
|---|---|---|---|
| GET | `/api/sensores` | Lista os sensores | Sim |
| GET | `/api/sensores/{id}` | Um sensor | Não |
| GET | `/api/medicoes` | Lista as medições, mais recentes primeiro (`?sensorId=1` filtra) | Sim |
| POST | `/api/medicoes/simular` | Simula uma coleta: gera o valor, calcula o status e grava | Sim (botão Simular medição) |
| POST | `/api/medicoes` | Registra uma coleta com valor informado | Não |

**Simular** (`POST /api/medicoes/simular`): corpo opcional `{ "sensorId": 2 }`. Sem `sensorId`, o servidor sorteia o sensor. O valor sai entre 50 e 115 (cerca de 60% normal, 25% alerta e 15% crítico), para que todos os status apareçam na demonstração. Resposta `201` com a medição criada.

```bash
curl -X POST http://localhost:8080/api/medicoes/simular -H "Content-Type: application/json" -d '{"sensorId": 2}'
curl -X POST http://localhost:8080/api/medicoes -H "Content-Type: application/json" -d '{"sensorId": 1, "valor": 92.5}'
```

Erros voltam em JSON, por exemplo `404 {"status": 404, "erro": "Sensor 9 não encontrado"}`.

## Banco de dados

H2 em arquivo na pasta `data/` (fora do Git). Os dados continuam lá depois de reiniciar a API; para começar do zero, pare a API e apague a pasta `data/`.

Console: http://localhost:8080/h2-console (JDBC URL `jdbc:h2:file:./data/sentinela`, usuário `sa`, sem senha).

## Integração com o app

- CORS liberado com `@CrossOrigin` nos dois controllers.
- `BASE_URL` no app: `http://localhost:8080` (web/iOS), `http://10.0.2.2:8080` (emulador Android) ou `http://IP-DO-COMPUTADOR:8080` (celular). Veja o README do app.
- Com a API parada, o app mostra a mensagem de erro e não exibe medições.

## Problemas comuns

- **`./mvnw: Permission denied`** (macOS/Linux): `chmod +x mvnw`.
- **Porta 8080 ocupada:** encerre o outro processo ou troque `server.port` em `application.properties` (e a `BASE_URL` do app).
- **Celular não acessa a API:** libere a porta 8080 no firewall do computador.
