package com.example.testefrontiemb.models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter @Setter
public class PeriodoPrestacaoId implements Serializable {
    private int ano;
    private int semestre;

    public PeriodoPrestacaoId(int ano, int semestre) {
        this.ano = ano;
        this.semestre = semestre;
    }

    public PeriodoPrestacaoId() {

    }
}
