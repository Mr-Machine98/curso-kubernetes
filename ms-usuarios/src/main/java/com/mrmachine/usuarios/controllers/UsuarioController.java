package com.mrmachine.usuarios.controllers;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.mrmachine.usuarios.models.entity.Usuario;
import com.mrmachine.usuarios.services.UsuarioService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class UsuarioController {
	
	private UsuarioService service;
	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private Environment env;
	
	public UsuarioController(UsuarioService service) {
		this.service = service;
	}
	
	@GetMapping("/crash")
	public String crash() {
		((ConfigurableApplicationContext) this.context).close();
		return "Stopped!!!";
	}
	
	@GetMapping("/all-info-pod")
	public ResponseEntity<?> findAllAddInfoPod() {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("users", service.findAll());
		body.put("pod_info", String.format("pod_name: %s, pod_ip: %s", env.getProperty("MY_POD_NAME"), env.getProperty("MY_POD_IP")));
		body.put("texto", env.getProperty("config.texto"));
		return ResponseEntity.ok(body);
	}
	
	@GetMapping
	public List<Usuario> findAll() {
		return service.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		Optional<Usuario> optional = service.findById(id);
		if (optional.isPresent()) {
			return ResponseEntity.ok(optional.get());
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/usuarios-por-curso")
	public ResponseEntity<?> findUsersByIds(@RequestParam List<Long> ids) {
		return ResponseEntity.ok(service.findByIds(ids));
	}
	
	@PostMapping
	public ResponseEntity<?> save(@Valid @RequestBody Usuario usuario, BindingResult result) {
		if(!usuario.getEmail().isEmpty() && service.findByEmail(usuario.getEmail()).isPresent()) return ResponseEntity.badRequest().body(Collections.singletonMap("email", "Email value is already exists!"));
		if (result.hasErrors()) return validar(result);
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(usuario));
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @Valid  @RequestBody Usuario usuario, BindingResult result) {
		if (result.hasErrors()) return validar(result);
		Optional<Usuario> optional = service.findById(id);
		if (optional.isPresent()) {
			Usuario usuarioDb = optional.get();
			if (!usuario.getEmail().isEmpty() && !usuario.getEmail().equalsIgnoreCase(usuarioDb.getEmail()) && service.findByEmail(usuario.getEmail()).isPresent()) 
				return ResponseEntity.badRequest().body(Collections.singletonMap("email", "Email value is already exists!!!"));
			usuarioDb.setNombre(usuario.getNombre());
			usuarioDb.setEmail(usuario.getEmail());
			usuarioDb.setPassword(usuario.getPassword());
			return ResponseEntity.status(HttpStatus.OK).body(service.save(usuarioDb));
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
	
	private ResponseEntity<?> validar(BindingResult result) {
		Map<String, String> errores = new HashMap<>();
		result
			.getFieldErrors()
			.forEach(err -> errores
					.put(err.getField(), String.format("Field %s %s", err.getField(), err.getDefaultMessage())));
		return ResponseEntity.badRequest().body(errores);
	}
	
}
