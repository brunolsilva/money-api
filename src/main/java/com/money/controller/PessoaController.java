package com.money.controller;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.money.model.Pessoa;
import com.money.repository.PessoaRepository;
import com.money.service.PessoaService;

@RequestMapping("/pessoas")
@RestController
public class PessoaController {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private PessoaService pessoaService;
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
	public ResponseEntity<Pessoa> create(@Valid @RequestBody Pessoa pessoa, HttpServletResponse response) {
		Pessoa pessoaSalva = pessoaRepository.save(pessoa);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
				.buildAndExpand(pessoaSalva.getCodigo()).toUri();
		response.setHeader("Location",uri.toASCIIString());
		return ResponseEntity.status(HttpStatus.CREATED).body(pessoaSalva);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
	public ResponseEntity<Pessoa> findById(@PathVariable Long id) {
		Pessoa pessoa = pessoaRepository.findOne(id);
		return pessoa != null ? ResponseEntity.ok(pessoa): ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_PESSOA') and #oauth2.hasScope('write')")
	public void remove(@PathVariable Long id) {
		pessoaRepository.delete(id);
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
	public ResponseEntity<Pessoa> update(@PathVariable Long id, @Valid @RequestBody Pessoa pessoa) {
		Pessoa pessoaSalva = pessoaService.update(id, pessoa);
		return ResponseEntity.ok(pessoaSalva);
	}
	
	@PutMapping("/{id}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
	public void updateActive(@PathVariable Long id, @RequestBody Boolean ativo) {
		pessoaService.atualizarAtivo(id, ativo);
	}
	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA')")
	public Page<Pessoa> search(@RequestParam(required = false, defaultValue = "%") String name, Pageable pageable) {
		return pessoaRepository.findByNomeContaining(name, pageable);
	}
}
