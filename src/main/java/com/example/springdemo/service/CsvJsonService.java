package com.example.springdemo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Service
public class CsvJsonService {

    private final ObjectMapper objectMapper;
    private final CsvMapper csvMapper = new CsvMapper();

    public CsvJsonService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /** Converte un CSV (con header nella prima riga) in un JSON array di oggetti. */
    public byte[] convertiCsvInJson(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Il file CSV è vuoto o mancante");
        }

        CsvSchema schema = CsvSchema.emptySchema().withHeader();

        try (InputStream is = file.getInputStream()) {
            MappingIterator<Map<String, String>> it = csvMapper
                    .readerFor(new TypeReference<Map<String, String>>() {})
                    .with(schema)
                    .readValues(is);

            List<Map<String, String>> righe = it.readAll();
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(righe);
        }
    }
}