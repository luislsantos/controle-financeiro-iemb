package com.example.testefrontiemb.respository;

import com.example.testefrontiemb.models.RegistroContabil;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface RegistroRepository extends JpaRepository<RegistroContabil,Long> {
    List<RegistroContabil> findByAnoPrestacaoAndSemestrePrestacao(int anoPrestacao, int semestrePrestacao, Sort sort);
}
