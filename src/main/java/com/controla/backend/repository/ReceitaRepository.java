package com.controla.backend.repository;
import com.controla.backend.entity.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Repository
public interface ReceitaRepository  extends JpaRepository<Receita, Long> {
    @Query("SELECT SUM(r.valor) FROM Receita r WHERE r.user.email = :email")
    BigDecimal somarReceitasPorUsuario(@Param("email") String email);

    @Query("""
    SELECT COALESCE(SUM(r.valor), 0)
    FROM Receita r
    WHERE r.user.email = :email
    AND r.data BETWEEN :dataInicial AND :dataFinal
""")
    BigDecimal somarReceitasPorUsuarioNoPeriodo(
            @Param("email") String email,
            @Param("dataInicial") LocalDate dataInicial,
            @Param("dataFinal") LocalDate dataFinal
    );

    List<Receita> findTop3ByUserEmailOrderByDataDesc(String email);
    List<Receita> findByUserEmailOrderByDataDesc(String email);



}
