package com.example.testefrontiemb.respository;

import com.example.testefrontiemb.models.PeriodoPrestacao;
import com.example.testefrontiemb.models.PeriodoPrestacaoId;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PeriodoRepository  extends JpaRepository<PeriodoPrestacao, PeriodoPrestacaoId> {

    List<PeriodoPrestacao> findBySemestre(int semestre, Sort sort);
}
