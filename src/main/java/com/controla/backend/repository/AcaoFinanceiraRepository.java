package com.controla.backend.repository;

import com.controla.backend.entity.AcaoFinanceira;
import com.controla.backend.entity.TipoAcaoFinanceira;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface AcaoFinanceiraRepository extends JpaRepository<AcaoFinanceira, Long> {
    @Query("""
    SELECT COALESCE(SUM(a.valor), 0)
    FROM AcaoFinanceira a
    WHERE a.usuario.email = :email
    AND a.tipo = :tipo
""")
    BigDecimal somarPorTipo(
            @Param("email") String email,
            @Param("tipo") TipoAcaoFinanceira tipo
    );

    List<AcaoFinanceira> findTop3ByUsuarioEmailOrderByDataDesc(String email);
    List<AcaoFinanceira> findByUsuarioEmailOrderByDataDesc(String email);
}