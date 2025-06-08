package com.mrmachine.usuarios.services;

import java.util.List;
import java.util.Optional;

import com.mrmachine.usuarios.models.entity.Usuario;

public interface UsuarioService {
	List<Usuario> findAll();
	Optional<Usuario> findById(Long id);
	List<Usuario> findByIds(Iterable<Long> ids);
	Usuario save(Usuario usuario);
	void delete(Long id);
	Optional<Usuario> findByEmail(String email);
}
