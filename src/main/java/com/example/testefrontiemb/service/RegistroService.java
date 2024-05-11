package com.example.testefrontiemb.service;

import com.example.testefrontiemb.models.RegistroContabil;
import org.springframework.stereotype.Service;
import com.example.testefrontiemb.respository.RegistroRepository;

import java.util.ArrayList;

@Service
public class RegistroService {
    private final RegistroRepository registroRepository;

    public RegistroService(RegistroRepository registroRepository) {
        this.registroRepository = registroRepository;
    }
    public void salvarRegistro(RegistroContabil registroContabil) {
        registroRepository.save(registroContabil);
        System.out.println("Salvou no Repositório");
    }
    public void lerRegistrosParaTeste(){

        System.out.println("Lendo os Registros");
        ArrayList<RegistroContabil> registros = (ArrayList<RegistroContabil>) registroRepository.findAll();
        for(RegistroContabil registro:registros) {
        System.out.println(registro.getId());
        System.out.println(registro.getTitulo());
        System.out.println(registro.getDescricao());
        System.out.println(registro.getData());
        }
    }
    public ArrayList<RegistroContabil> lerTudo() {
        return (ArrayList<RegistroContabil>)  registroRepository.findAll();
    }
}
