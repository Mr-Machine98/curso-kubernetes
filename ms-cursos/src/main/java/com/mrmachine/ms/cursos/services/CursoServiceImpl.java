package com.mrmachine.ms.cursos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mrmachine.ms.cursos.clients.UsuarioClientRest;
import com.mrmachine.ms.cursos.models.Usuario;
import com.mrmachine.ms.cursos.models.entities.Curso;
import com.mrmachine.ms.cursos.models.entities.CursoUsuario;
import com.mrmachine.ms.cursos.repositories.CursoRepository;

@Service
public class CursoServiceImpl implements CursoService {
	
	private CursoRepository repository;
	
	private UsuarioClientRest client;
	
	public CursoServiceImpl(CursoRepository repository, UsuarioClientRest client) {
		this.repository = repository;
		this.client = client;
	}

	@Override
	public List<Curso> findAll() {
		return (List<Curso>) repository.findAll();
	}

	@Override
	public Optional<Curso> findById(Long id) {
		return repository.findById(id);
	}

	@Override
	@Transactional
	public Curso save(Curso c) {
		return repository.save(c);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		repository.deleteById(id);
	}

	@Override
	@Transactional
	public Optional<Usuario> addUser(Usuario u, Long courseId) {
		Optional<Curso> opt = repository.findById(courseId);
		if (opt.isPresent()) {	
			Usuario usr = client.detail(u.getId());
			Curso curso = opt.get();
			CursoUsuario enroll = new CursoUsuario();
			enroll.setUsuarioId(usr.getId());
			curso.addCursoUsuario(enroll);
			repository.save(curso);
			return Optional.of(usr);
		}
		return Optional.empty();
	}

	@Override
	@Transactional
	public Optional<Usuario> createUser(Usuario u, Long courseId) {
		Optional<Curso> opt = repository.findById(courseId);
		if (opt.isPresent()) {	
			Usuario newUsr = client.create(u);
			Curso curso = opt.get();
			CursoUsuario enroll = new CursoUsuario();
			enroll.setUsuarioId(newUsr.getId());
			curso.addCursoUsuario(enroll);
			repository.save(curso);
			return Optional.of(newUsr);
		}
		return Optional.empty();
	}

	@Override
	@Transactional
	public Optional<Usuario> deleteUser(Usuario u, Long courseId) {
		Optional<Curso> opt = repository.findById(courseId);
		if (opt.isPresent()) {	
			Usuario usr = client.detail(u.getId());
			Curso curso = opt.get();
			CursoUsuario enroll = new CursoUsuario();
			enroll.setUsuarioId(usr.getId());
			curso.removeCursoUsuario(enroll);
			repository.save(curso);
			return Optional.of(usr);
		}
		return Optional.empty();
	}

	@Override
	public Optional<Curso> findByIdWithUsers(Long id) {
		Optional<Curso> opt = repository.findById(id);
		if (opt.isPresent()) {
			Curso c = opt.get();
			if (!c.getCursoUsuarios().isEmpty()) {
				List<Long> ids = c.getCursoUsuarios()
						.stream()
						.map(cu -> cu.getUsuarioId())
						.toList();
				List<Usuario> users = client.getStudentsByCourse(ids);
				c.setUsuarios(users);
			}
			return Optional.of(c);
		}
		return Optional.empty();
	}

	@Override
	@Transactional
	public void deleteCursoUsuarioById(Long id) {
		repository.deleteCursoUsuarioById(id);
	}

}
