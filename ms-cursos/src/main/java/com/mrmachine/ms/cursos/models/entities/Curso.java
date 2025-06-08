package com.mrmachine.ms.cursos.models.entities;

import java.util.ArrayList;
import java.util.List;

import com.mrmachine.ms.cursos.models.Usuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "cursos")
public class Curso {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	private String nombre;
	
	@JoinColumn(name = "curso_id")
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CursoUsuario> cursoUsuarios;
	
	@Transient
	private List<Usuario> usuarios;
	
	public Curso() {
		cursoUsuarios = new ArrayList<CursoUsuario>();
		usuarios = new ArrayList<Usuario>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<CursoUsuario> getCursoUsuarios() {
		return cursoUsuarios;
	}

	public void addCursoUsuario(CursoUsuario cursoUsuario) {
		cursoUsuarios.add(cursoUsuario);
	}
	
	public void removeCursoUsuario(CursoUsuario cursoUsuario) {
		cursoUsuarios.remove(cursoUsuario);
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	
}
