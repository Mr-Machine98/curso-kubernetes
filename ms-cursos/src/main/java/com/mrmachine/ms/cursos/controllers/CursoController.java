package com.mrmachine.ms.cursos.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.mrmachine.ms.cursos.models.Usuario;
import com.mrmachine.ms.cursos.models.entities.Curso;
import com.mrmachine.ms.cursos.services.CursoService;

import feign.FeignException;
import jakarta.validation.Valid;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class CursoController {
	
	private CursoService service;
	
	public CursoController(CursoService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<List<Curso>> findAll() {
		return ResponseEntity.ok(service.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		Optional<Curso> opt = service.findByIdWithUsers(id);
		if (opt.isPresent()) {
			return ResponseEntity.ok(opt.get());
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<?> save(@Valid @RequestBody Curso curso, BindingResult result) {
		if (result.hasErrors()) return validar(result);
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(curso));
	}
	
	@PostMapping("/crear-usuario/{courseId}")
	public ResponseEntity<?> createUser(@PathVariable Long courseId, @RequestBody Usuario usr) {
		Optional<Usuario> opt = null;
		try {
			opt = service.createUser(usr, courseId);
		} catch (FeignException e) {
			return ResponseEntity
					.status(HttpStatus.SERVICE_UNAVAILABLE)
					.body(Collections.singletonMap("message", String.format("The user couldn't be created or there had been a mistake with ms-usuarios: %s", e.getMessage())));
		}
		if (opt.isPresent()) {
			return ResponseEntity.status(HttpStatus.CREATED).body(opt.get());
		}
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/asignar-usuario/{courseId}")
	public ResponseEntity<?> enroll(@PathVariable Long courseId, @RequestBody Usuario usr) {
		Optional<Usuario> opt = null;
		try {
			opt = service.addUser(usr, courseId);
		} catch (FeignException e) {
			return ResponseEntity
					.status(HttpStatus.SERVICE_UNAVAILABLE)
					.body(Collections.singletonMap("message", String.format("User not found by id or there had been a mistake with ms-usuarios: %s", e.getMessage())));
		}
		if (opt.isPresent()) {
			return ResponseEntity.status(HttpStatus.CREATED).body(opt.get());
		}
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@Valid @PathVariable Long id, @RequestBody Curso curso, BindingResult result) {
		if (result.hasErrors()) return validar(result);
		Optional<Curso> opt = service.findById(id);
		if (opt.isPresent()) {
			Curso cursoDb = opt.get();
			cursoDb.setNombre(curso.getNombre());
			return ResponseEntity.ok(service.save(cursoDb));
		}
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		if (service.findById(id).isPresent()) {
			service.delete(id);
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/eliminar-usuario/{courseId}")
	public ResponseEntity<?> deleteUser(@PathVariable Long courseId, @RequestBody Usuario usr) {
		Optional<Usuario> opt = null;
		try {
			opt = service.deleteUser(usr, courseId);
		} catch (FeignException e) {
			return ResponseEntity
					.status(HttpStatus.SERVICE_UNAVAILABLE)
					.body(Collections.singletonMap("message", String.format("User not found by id or there had been a mistake with ms-usuarios: %s", e.getMessage())));
		}
		if (opt.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(opt.get());
		}
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/eliminar-curso-usuario/{id}")
	public ResponseEntity<?> deleteCursoUsuario(@PathVariable Long id) {
		service.deleteCursoUsuarioById(id);
		return ResponseEntity.noContent().build();
	}
	
	private ResponseEntity<?> validar(BindingResult result) {
		Map<String, String> errores = new HashMap<>();
		result
			.getFieldErrors()
			.forEach(err -> errores
					.put(err.getField(), String.format("Field %s %s", err.getField(), err.getDefaultMessage())));
		return ResponseEntity.badRequest().body(errores);
	}
	
}
