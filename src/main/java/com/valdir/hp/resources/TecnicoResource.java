package com.valdir.hp.resources;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.valdir.hp.dtos.TecnicoDTO;
import com.valdir.hp.model.Pessoa;
import com.valdir.hp.model.Tecnico;
import com.valdir.hp.services.TecnicoService;

@RestController
@RequestMapping(value = "/tecnicos")
public class TecnicoResource {

	@Autowired
	private TecnicoService service;

	/**
	 * Busca um Tecnico por ID
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<TecnicoDTO> findById(@PathVariable Integer id) {
		Tecnico obj = service.findById(id);
		return ResponseEntity.ok().body(new TecnicoDTO(obj));
	}
	
	/**
	 * Busca um Tecnico por E-MAIL
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/email")
	public ResponseEntity<TecnicoDTO> findByEmail(@RequestParam String email) {
		Optional<Pessoa> obj = service.findByEmail(email);
		Tecnico tec = service.findById(obj.get().getId());
		return ResponseEntity.ok().body(new TecnicoDTO(tec));
	}

	/**
	 * Lista todos os Tecnicos do banco
	 * 
	 * @return
	 */
	@GetMapping
	public ResponseEntity<List<TecnicoDTO>> findAll() {
		List<Tecnico> list = service.findAll();
		List<TecnicoDTO> listDTO = list.stream().map(obj -> new TecnicoDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDTO);
	}

	/**
	 * Cria um novo Tecnico
	 * 
	 * @param objDTO
	 * @return
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping
	public ResponseEntity<TecnicoDTO> create(@Valid @RequestBody TecnicoDTO objDTO) {
		Tecnico obj = service.create(objDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).body(new TecnicoDTO(obj));
	}

	/**
	 * Atualiza as informações de um Tecnico
	 * 
	 * @param id
	 * @param objDTO
	 * @return
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@PutMapping(value = "/{id}")
	public ResponseEntity<TecnicoDTO> update(@PathVariable Integer id, @Valid @RequestBody TecnicoDTO objDTO) {
		Tecnico obj = service.update(id, objDTO);
		return ResponseEntity.ok().body(new TecnicoDTO(obj));
	}

	/**
	 * Deleta um Tecnico
	 * 
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}

}
