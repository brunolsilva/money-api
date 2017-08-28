package com.money.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.money.model.Categoria;
import com.money.repository.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Categoria update(long id, Categoria categoria) {
		Categoria categoriaExistente = categoriaRepository.findOne(id);
		BeanUtils.copyProperties(categoria, categoriaExistente, "codigo");
		return categoriaRepository.save(categoriaExistente);
	}
	
}
