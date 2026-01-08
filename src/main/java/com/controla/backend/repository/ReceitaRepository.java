package com.controla.backend.repository;
import com.controla.backend.entity.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceitaRepository  extends JpaRepository<Receita, Long> {

}
