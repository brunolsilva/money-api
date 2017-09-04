package com.money.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.money.model.Lancamento;
import com.money.repository.lancamento.LancamentoRepositoryQuery;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {

}