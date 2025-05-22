package com.mrmachine.usuarios.repositories;

import org.springframework.data.repository.CrudRepository;

import com.mrmachine.usuarios.models.entity.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long>{

}
