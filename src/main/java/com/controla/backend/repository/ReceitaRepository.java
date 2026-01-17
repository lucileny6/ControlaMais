package com.controla.backend.repository;
import com.controla.backend.entity.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;


import java.math.BigDecimal;
import java.util.List;


@Repository
public interface ReceitaRepository  extends JpaRepository<Receita, Long> {
    @Query("SELECT SUM(r.valor) FROM Receita r WHERE r.user.email = :email")
    BigDecimal somarReceitasPorUsuario(@Param("email") String email);

    List<Receita> findTop3ByUserEmailOrderByDataDesc(String email);



}
