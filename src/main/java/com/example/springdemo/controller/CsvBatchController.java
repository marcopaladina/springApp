package com.example.springdemo.controller;

import com.example.springdemo.service.CsvBatchImportService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/import")
public class CsvBatchController {

    private final CsvBatchImportService csvBatchImportService;

    public CsvBatchController(CsvBatchImportService csvBatchImportService) {
        this.csvBatchImportService = csvBatchImportService;
    }

    @PostMapping("/csv-batch")
    public String uploadCsvBatch(@RequestParam("file") MultipartFile file) {
        try {
            csvBatchImportService.importCsvBatch(file);
            return "Importazione batch completata con successo";
        } catch (Exception e) {
            return "Errore durante l'importazione batch: " + e.getMessage();
        }
    }
}
