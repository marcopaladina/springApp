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
    private final CsvMapper csvMapper;

    public CsvJsonService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.csvMapper = new CsvMapper();
    }

    public byte[] convertiCsvInJson(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Il file CSV è vuoto o mancante"
            );
        }

        CsvSchema schema = CsvSchema.emptySchema()
                .withHeader()
                .withColumnSeparator(';')
                .withQuoteChar('"');

        try (InputStream is = file.getInputStream()) {

            MappingIterator<Map<String, String>> iterator = csvMapper
                    .readerFor(
                            new TypeReference<Map<String, String>>() {}
                    )
                    .with(schema)
                    .readValues(is);

            List<Map<String, String>> righe = iterator.readAll();

            return objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsBytes(righe);
        }
    }
}