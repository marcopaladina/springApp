package com.example.batchservice.model;

import jakarta.persistence.*;

/**
 * Entity JPA che rappresenta un prodotto salvato su H2.
 */
@Entity
@Table(name = "prodotti")
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codice;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Double prezzo;

    @Column(nullable = false)
    private Integer quantita;

    public Prodotto() {
    }

    public Prodotto(String codice, String nome, Double prezzo, Integer quantita) {
        this.codice = codice;
        this.nome = nome;
        this.prezzo = prezzo;
        this.quantita = quantita;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Double prezzo) {
        this.prezzo = prezzo;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    @Override
    public String toString() {
        return "Prodotto{" +
                "id=" + id +
                ", codice='" + codice + '\'' +
                ", nome='" + nome + '\'' +
                ", prezzo=" + prezzo +
                ", quantita=" + quantita +
                '}';
    }
}
