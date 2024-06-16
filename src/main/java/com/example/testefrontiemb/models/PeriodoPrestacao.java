package com.example.testefrontiemb.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@IdClass(PeriodoPrestacaoId.class)
@Getter @Setter
public class PeriodoPrestacao {
    @Id
    private int ano;
    @Id
    private int semestre;

    public PeriodoPrestacao(int ano, int semestre) {
        this.ano = ano;
        this.semestre = semestre;
    }

    public PeriodoPrestacao() {

    }
}
