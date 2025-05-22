package com.mrmachine.usuarios.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mrmachine.usuarios.models.entity.Usuario;
import com.mrmachine.usuarios.repositories.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	private UsuarioRepository repository;
	
	public UsuarioServiceImpl(UsuarioRepository repository) {
		this.repository = repository;
	}

	@Override
	public List<Usuario> findAll() {
		return (List<Usuario>) repository.findAll();
	}

	@Override
	public Optional<Usuario> findById(Long id) {
		return repository.findById(id);
	}

	@Override
	@Transactional
	public Usuario save(Usuario usuario) {
		return repository.save(usuario);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		repository.deleteById(id);
	}

}
