package com.mrmachine.ms.cursos.services;

import java.util.List;
import java.util.Optional;

import com.mrmachine.ms.cursos.models.Usuario;
import com.mrmachine.ms.cursos.models.entities.Curso;

public interface CursoService {
	List<Curso> findAll();
	Optional<Curso> findById(Long id);
	Optional<Curso> findByIdWithUsers(Long id);
	Curso save(Curso c);
	void delete(Long id);
	void deleteCursoUsuarioById(Long id);
	Optional<Usuario> addUser(Usuario u, Long courseId);
	Optional<Usuario> createUser(Usuario u, Long courseId);
	Optional<Usuario> deleteUser(Usuario u, Long courseId);
	
}
