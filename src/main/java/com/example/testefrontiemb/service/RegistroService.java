package com.example.testefrontiemb.service;

import com.example.testefrontiemb.models.RegistroContabil;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.example.testefrontiemb.respository.RegistroRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class RegistroService {
    private final RegistroRepository registroRepository;

    public RegistroService(RegistroRepository registroRepository) {
        this.registroRepository = registroRepository;
    }
    public RegistroContabil salvarRegistro(RegistroContabil registroContabil) {
        //System.out.println("Salvou no Repositório");
        return registroRepository.save(registroContabil);
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

    public ArrayList<RegistroContabil> buscaPorAnoESemestre(int anoPrestacao, int semestrePrestacao) {
        List<RegistroContabil> registros = registroRepository.findByAnoPrestacaoAndSemestrePrestacao(anoPrestacao, semestrePrestacao, Sort.by(Sort.Direction.ASC, "anoPrestacao"));
        return new ArrayList<>(registros);
    }

    public void deletarRegistro(RegistroContabil registroDeletar) {
        registroRepository.delete(registroDeletar);
    }
}
