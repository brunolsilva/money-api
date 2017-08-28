package com.money.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.money.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
