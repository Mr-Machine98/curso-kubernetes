package com.mrmachine.ms.cursos.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.mrmachine.ms.cursos.models.entities.Curso;

public interface CursoRepository extends CrudRepository<Curso, Long> {
	
	@Modifying
	@Query("delete from CursoUsuario cu where cu.usuarioId=?1")
	void deleteCursoUsuarioById(Long id);
	
}
