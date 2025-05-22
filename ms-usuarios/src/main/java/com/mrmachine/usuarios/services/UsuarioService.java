package com.mrmachine.usuarios.services;

import java.util.List;
import java.util.Optional;

import com.mrmachine.usuarios.models.entity.Usuario;

public interface UsuarioService {
	List<Usuario> findAll();
	Optional<Usuario> findById(Long id);
	Usuario save(Usuario usuario);
	void delete(Long id);
}
