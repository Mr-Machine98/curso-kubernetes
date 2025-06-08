package com.mrmachine.usuarios.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-cursos", url = "localhost:8002")
public interface CursoClienteRest {

	@DeleteMapping("/eliminar-curso-usuario/{id}")
	void deleteCursoUsuario(@PathVariable Long id);
}
