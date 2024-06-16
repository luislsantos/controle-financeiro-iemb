package com.example.testefrontiemb.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table
public class RegistroContabil {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter private Long id;
    @Getter @Setter
    private String titulo;
    @Getter @Setter
    private String descricao;
    @Getter @Setter
    private String tipo;
    @Getter @Setter
    private LocalDate data;
    @Getter @Setter
    private double valor;
    @Getter @Setter
    private String origemOuDestinacao;
    @Getter @Setter
    private String cpfCnpj;
    @Getter @Setter
    private String numNotaFiscal;
    @Getter @Setter
    private String pathScanNotaFiscal;
    @Getter @Setter
    private int anoPrestacao;
    @Getter @Setter
    private int semestrePrestacao;

    protected RegistroContabil(String titulo, String descricao, String tipo, LocalDate data, Double valor) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
        this.data = data;
        this.valor = valor;
    }

    public RegistroContabil(String titulo, String descricao, String tipo, LocalDate data, Double valor, String origemOuDestinacao, String cpfCnpj, String numNotaFiscal, String pathScanNotaFiscal, int anoPrestacao, int semestrePrestacao) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
        this.data = data;
        this.valor = valor;
        this.origemOuDestinacao = origemOuDestinacao;
        this.cpfCnpj = cpfCnpj;
        this.numNotaFiscal = numNotaFiscal;
        this.pathScanNotaFiscal = pathScanNotaFiscal;
        this.anoPrestacao = anoPrestacao;
        this.semestrePrestacao = semestrePrestacao;
    }

    protected RegistroContabil() {

    }
}
