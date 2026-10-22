package com.example.springdemo.controller;

import com.example.springdemo.service.CsvJsonService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/convert")
public class CsvJsonController {

    private final CsvJsonService service;

    public CsvJsonController(CsvJsonService service) {
        this.service = service;
    }

    @PostMapping(value = "/csv-to-json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> csvToJson(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] json = service.convertiCsvInJson(file);

        String nome = file.getOriginalFilename() != null
                ? file.getOriginalFilename().replaceAll("(?i)\\.csv$", "") + ".json"
                : "output.json";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(nome).build().toString())
                .body(json);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> badRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
