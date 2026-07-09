package com.example.batchservice.dto;

/**
 * Oggetto "grezzo" su cui viene mappata ogni riga del file CSV,
 * prima della validazione/trasformazione fatta dal Processor.
 */
public class ProdottoCsv {

    private String codice;
    private String nome;
    private String prezzo;
    private String quantita;

    public ProdottoCsv() {
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

    public String getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(String prezzo) {
        this.prezzo = prezzo;
    }

    public String getQuantita() {
        return quantita;
    }

    public void setQuantita(String quantita) {
        this.quantita = quantita;
    }

    @Override
    public String toString() {
        return "ProdottoCsv{" +
                "codice='" + codice + '\'' +
                ", nome='" + nome + '\'' +
                ", prezzo='" + prezzo + '\'' +
                ", quantita='" + quantita + '\'' +
                '}';
    }
}
