package com.money.controller;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.money.model.Categoria;
import com.money.repository.CategoriaRepository;
import com.money.service.CategoriaService;


@RestController
@RequestMapping("/categoria")
public class CategoriaController {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private CategoriaService categoriaService;
	
	@GetMapping
	public ResponseEntity<List<Categoria>> list() {
		List<Categoria> categorias = categoriaRepository.findAll();
		return !categorias.isEmpty() ? ResponseEntity.ok(categorias): ResponseEntity.noContent().build();
	}
	
	@PostMapping
	public ResponseEntity<Categoria> create(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		categoria = categoriaRepository.save(categoria);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
				.buildAndExpand(categoria.getCodigo()).toUri();
		response.setHeader("Location",uri.toASCIIString());
		
		return ResponseEntity.created(uri).body(categoria);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Categoria> findById(@PathVariable long id) {
		Categoria categoria = categoriaRepository.findOne(id);
		return categoria != null ? ResponseEntity.ok(categoria) : ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Categoria> update(@PathVariable long id, @Valid @RequestBody Categoria categoria) {
		Categoria categoriaExistente = categoriaService.update(id,categoria);
		return ResponseEntity.ok(categoriaExistente);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void delete(@PathVariable long id) {
		categoriaRepository.delete(id);
	}
}