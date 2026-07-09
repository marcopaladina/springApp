package com.example.batchservice.controller;

import com.example.batchservice.model.Prodotto;
import com.example.batchservice.repository.ProdottoRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Espone gli endpoint REST per:
 * - avviare manualmente il job batch di import
 * - consultare i prodotti caricati su H2
 */
@RestController
@RequestMapping("/api/batch")
public class JobLauncherController {

    private final JobLauncher jobLauncher;
    private final Job importProdottiJob;
    private final ProdottoRepository prodottoRepository;

    public JobLauncherController(JobLauncher jobLauncher,
                                  Job importProdottiJob,
                                  ProdottoRepository prodottoRepository) {
        this.jobLauncher = jobLauncher;
        this.importProdottiJob = importProdottiJob;
        this.prodottoRepository = prodottoRepository;
    }

    /**
     * Avvia il job di import del file CSV configurato in application.yml
     * (batch.input.file). Ogni chiamata usa un parametro univoco (timestamp)
     * cosi' Spring Batch non la considera un'esecuzione duplicata.
     */
    @PostMapping("/start")
    public ResponseEntity<Map<String, Object>> startImport() {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution execution = jobLauncher.run(importProdottiJob, params);

            return ResponseEntity.ok(Map.of(
                    "jobId", execution.getJobId(),
                    "status", execution.getStatus().toString(),
                    "letti", execution.getStepExecutions().stream()
                            .mapToLong(se -> se.getReadCount()).sum(),
                    "scritti", execution.getStepExecutions().stream()
                            .mapToLong(se -> se.getWriteCount()).sum(),
                    "scartati", execution.getStepExecutions().stream()
                            .mapToLong(se -> se.getSkipCount()).sum()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("errore", e.getMessage()));
        }
    }

    /** Restituisce tutti i prodotti attualmente presenti su H2. */
    @GetMapping("/prodotti")
    public ResponseEntity<List<Prodotto>> getProdotti() {
        return ResponseEntity.ok(prodottoRepository.findAll());
    }
}
