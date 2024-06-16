package com.example.testefrontiemb.service;

import com.example.testefrontiemb.models.PeriodoPrestacao;
import com.example.testefrontiemb.models.RegistroContabil;
import com.example.testefrontiemb.respository.PeriodoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PeriodoService {
    private final PeriodoRepository periodoRepository;

    public PeriodoService(PeriodoRepository periodoRepository) {
        this.periodoRepository = periodoRepository;
    }

    public PeriodoPrestacao salvarPeriodo(PeriodoPrestacao periodoPrestacao) {
        return periodoRepository.save(periodoPrestacao);
    }

    public ArrayList<PeriodoPrestacao> lerTudo() {
        return (ArrayList<PeriodoPrestacao>) periodoRepository.findAll();
    }

    public ArrayList<PeriodoPrestacao> pegarUmPorSemestre() {
        List<PeriodoPrestacao> periodos = periodoRepository.findBySemestre(1, Sort.by(Sort.Direction.ASC, "ano"));
        return new ArrayList<>(periodos);
    }
}
