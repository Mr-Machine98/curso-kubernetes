package com.mrmachine.usuarios.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mrmachine.usuarios.clients.CursoClienteRest;
import com.mrmachine.usuarios.models.entity.Usuario;
import com.mrmachine.usuarios.repositories.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	private UsuarioRepository repository;
	
	private CursoClienteRest client;
	
	public UsuarioServiceImpl(UsuarioRepository repository, CursoClienteRest client) {
		this.repository = repository;
		this.client = client;
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
		client.deleteCursoUsuario(id);
	}

	@Override
	public Optional<Usuario> findByEmail(String email) {
		return repository.findByEmail(email);
	}

	@Override
	public List<Usuario> findByIds(Iterable<Long> ids) {
		return (List<Usuario>) repository.findAllById(ids);
	}

}
