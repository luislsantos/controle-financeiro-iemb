package com.example.testefrontiemb.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Anexo {
    String descricao;
    String path;

    @Override
    public String toString() {
        return "Anexo{" +
                "descricao='" + descricao + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
