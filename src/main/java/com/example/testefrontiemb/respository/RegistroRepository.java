package com.example.testefrontiemb.respository;

import com.example.testefrontiemb.models.RegistroContabil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface RegistroRepository extends JpaRepository<RegistroContabil,Long> {
}
