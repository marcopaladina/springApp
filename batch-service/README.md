# batch-service

Microservizio Spring Boot (Spring Batch + Spring Data JPA + H2) che legge un file
CSV di prodotti e lo carica in un database H2 in-memory.

## Stack
- Java 17
- Spring Boot 3.2.5
- Spring Batch 5
- H2 (in-memory)
- Maven

## Struttura del flusso batch

```
FlatFileItemReader  -->  ProdottoItemProcessor  -->  RepositoryItemWriter
 (legge il CSV)          (valida/trasforma)          (salva su H2 via JPA)
```

- **Reader**: legge `src/main/resources/data/prodotti.csv`, saltando l'header.
- **Processor**: valida ogni riga (codice/nome non vuoti, prezzo e quantità
  numerici e non negativi). Le righe non valide vengono scartate (skip) senza
  bloccare l'intero job, fino a un massimo di 50 righe scartate.
- **Writer**: salva le entity `Prodotto` nel database H2 tramite `ProdottoRepository`.

Spring Batch crea automaticamente anche le proprie tabelle interne di stato
(`BATCH_JOB_INSTANCE`, `BATCH_STEP_EXECUTION`, ecc.) su H2, grazie a
`spring.batch.jdbc.initialize-schema: always`.

## Come avviare

```bash
mvn spring-boot:run
```

L'app parte su `http://localhost:8080`. Il job **non** parte automaticamente
all'avvio (`spring.batch.job.enabled: false`): va lanciato tramite l'endpoint REST.

## Endpoint disponibili

### Avviare il job di import
```bash
curl -X POST http://localhost:8080/api/batch/start
```
Risposta di esempio:
```json
{
  "jobId": 1,
  "status": "COMPLETED",
  "letti": 8,
  "scritti": 8,
  "scartati": 0
}
```

### Consultare i prodotti caricati
```bash
curl http://localhost:8080/api/batch/prodotti
```

### Console H2 (per ispezionare il database a runtime)
Apri nel browser: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Utente: `sa`
- Password: (vuota)

## Cambiare il file da importare

Il percorso del CSV è configurabile in `application.yml`:
```yaml
batch:
  input:
    file: classpath:data/prodotti.csv
```
Puoi puntare a un file esterno, ad esempio:
```yaml
batch:
  input:
    file: file:/percorso/assoluto/mio_file.csv
```
oppure passarlo come argomento da riga di comando:
```bash
java -jar batch-service.jar --batch.input.file=file:/tmp/mio_file.csv
```

## Formato atteso del CSV

```csv
codice,nome,prezzo,quantita
P001,Tastiera meccanica,49.90,120
```

## Test

È incluso un test di integrazione (`BatchJobIntegrationTest`) che lancia il job
con `JobLauncherTestUtils` e verifica che i prodotti vengano effettivamente
persistiti su H2:

```bash
mvn test
```

## Note

- Il progetto usa `RepositoryItemWriter.save()`: se rilanci il job più volte con
  lo stesso CSV e i codici sono `unique`, otterrai un errore di violazione di
  vincolo sui duplicati. Per import ripetuti puoi aggiungere una logica di
  upsert nel writer, oppure svuotare la tabella tra un'esecuzione e l'altra.
- Nota bene: non ho potuto compilare il progetto in questo ambiente (Maven
  non è installato e non c'è accesso di rete per scaricare le dipendenze),
  quindi verifica la build sul tuo ambiente locale prima di metterlo in
  produzione.
