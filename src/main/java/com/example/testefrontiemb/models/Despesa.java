package com.example.testefrontiemb.models;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

@Entity
@Getter @Setter
public class Despesa extends RegistroContabil{

    private ArrayList<String> anexos;

   /* public Despesa(String titulo, String descricao, String data, String valor, String destinacao, String cpfCnpj, String numNotaFiscal, String pathScanNotaFiscal, ArrayList<String> anexos) {
        super(titulo, descricao, "Despesa", data, valor);
        this.destinacao = destinacao;
        this.cpfCnpj = cpfCnpj;
        this.numNotaFiscal = numNotaFiscal;
        this.pathScanNotaFiscal = pathScanNotaFiscal;
        this.anexos = anexos;
    }*/

    public Despesa(String titulo, String descricao, LocalDate data, double valor, String destinacao, String cpfCnpj, String numNotaFiscal, String pathScanNotaFiscal, ArrayList<String> anexos) {
        super(titulo, descricao, "Despesa", data, valor, destinacao, cpfCnpj, numNotaFiscal, pathScanNotaFiscal);
        this.anexos = anexos;
    }

    public Despesa() {

    }
}
