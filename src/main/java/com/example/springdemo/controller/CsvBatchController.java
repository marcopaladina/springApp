package com.example.springdemo.controller;

import com.example.springdemo.service.CsvBatchImportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/batch")
public class CsvBatchController {

    private static final Logger log = LoggerFactory.getLogger(PersonController.class);
    private final CsvBatchImportService service;

    public CsvBatchController(CsvBatchImportService service) {
        this.service = service;
    }

    @PostMapping("/import/csv-batch")
    public String uploadCsvBatch(@RequestParam("file") MultipartFile file) {
        try {
            service.importCsvBatch(file);
            return "Importazione batch completata con successo";
        } catch (Exception e) {
            return "Errore durante l'importazione batch: " + e.getMessage();
        }
    }
}
