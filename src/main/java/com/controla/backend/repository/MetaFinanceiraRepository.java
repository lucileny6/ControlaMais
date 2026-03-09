package com.controla.backend.repository;

import com.controla.backend.entity.MetaFinanceira;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MetaFinanceiraRepository extends JpaRepository<MetaFinanceira, Long> {

    List<MetaFinanceira> findByUserEmailAndAtivaTrue(String email);

    Optional<MetaFinanceira> findByIdAndUserEmailAndAtivaTrue(Long id, String email);
}
