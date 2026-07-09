package com.example.batchservice.repository;

import com.example.batchservice.model.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {

    boolean existsByCodice(String codice);

}
