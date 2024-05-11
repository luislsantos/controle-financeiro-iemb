package com.example.testefrontiemb.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    private String data;
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

    protected RegistroContabil(String titulo, String descricao, String tipo, String data, Double valor) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
        this.data = data;
        this.valor = valor;
    }

    public RegistroContabil(String titulo, String descricao, String tipo, String data, Double valor, String origemOuDestinacao, String cpfCnpj, String numNotaFiscal, String pathScanNotaFiscal) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.tipo = tipo;
        this.data = data;
        this.valor = valor;
        this.origemOuDestinacao = origemOuDestinacao;
        this.cpfCnpj = cpfCnpj;
        this.numNotaFiscal = numNotaFiscal;
        this.pathScanNotaFiscal = pathScanNotaFiscal;
    }

    protected RegistroContabil() {

    }
}
