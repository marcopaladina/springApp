package com.example.batchservice.processor;

import com.example.batchservice.dto.ProdottoCsv;
import com.example.batchservice.model.Prodotto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.ValidationException;

/**
 * Trasforma ogni riga letta dal CSV (ProdottoCsv) in un'entity Prodotto
 * pronta per essere persistita. Applica validazioni di base: righe non
 * valide vengono scartate (lanciando eccezione, gestita come "skip" nello Step).
 */
public class ProdottoItemProcessor implements ItemProcessor<ProdottoCsv, Prodotto> {

    private static final Logger log = LoggerFactory.getLogger(ProdottoItemProcessor.class);

    @Override
    public Prodotto process(ProdottoCsv item) throws Exception {

        if (item.getCodice() == null || item.getCodice().isBlank()) {
            throw new ValidationException("Riga scartata: codice prodotto mancante -> " + item);
        }
        if (item.getNome() == null || item.getNome().isBlank()) {
            throw new ValidationException("Riga scartata: nome prodotto mancante -> " + item);
        }

        double prezzo;
        int quantita;
        try {
            prezzo = Double.parseDouble(item.getPrezzo().trim().replace(",", "."));
        } catch (NumberFormatException e) {
            throw new ValidationException("Riga scartata: prezzo non numerico -> " + item);
        }
        try {
            quantita = Integer.parseInt(item.getQuantita().trim());
        } catch (NumberFormatException e) {
            throw new ValidationException("Riga scartata: quantita non numerica -> " + item);
        }

        if (prezzo < 0 || quantita < 0) {
            throw new ValidationException("Riga scartata: valori negativi non ammessi -> " + item);
        }

        Prodotto prodotto = new Prodotto(
                item.getCodice().trim(),
                item.getNome().trim(),
                prezzo,
                quantita
        );

        log.debug("Trasformato: {} -> {}", item, prodotto);
        return prodotto;
    }
}
