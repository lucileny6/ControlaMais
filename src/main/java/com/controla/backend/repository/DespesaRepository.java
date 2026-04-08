package com.controla.backend.repository;
import com.controla.backend.entity.Despesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository


public interface DespesaRepository extends JpaRepository <Despesa, Long>{
    @Query("SELECT SUM(r.valor) FROM Despesa r WHERE r.user.email = :email")
    BigDecimal somarDespesasPorUsuario(@Param("email") String email);

    @Query("""
    SELECT COALESCE(SUM(d.valor), 0)
    FROM Despesa d
    WHERE d.user.email = :email
    AND d.data BETWEEN :dataInicial AND :dataFinal
""")
    BigDecimal somarDespesasPorUsuarioNoPeriodo(
            @Param("email") String email,
            @Param("dataInicial") LocalDate dataInicial,
            @Param("dataFinal") LocalDate dataFinal
    );

    List<Despesa> findTop3ByUserEmailOrderByDataDesc(String email);
    List<Despesa> findByUserEmailOrderByDataDesc(String email);

}
