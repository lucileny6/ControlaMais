package com.controla.backend.repository;

import com.controla.backend.entity.MetaFinanceira;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MetaFinanceiraRepository extends JpaRepository<MetaFinanceira, Long> {

    List<MetaFinanceira> findByUserEmailAndAtivaTrue(String email);



}
