package com.controla.backend.repository;
import com.controla.backend.entity.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository


public interface DespesaRepository extends JpaRepository <Despesa, Long>{
}
