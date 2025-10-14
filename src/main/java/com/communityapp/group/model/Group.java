package com.communityapp.group.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "groups")
public class Group {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 60)
	private String nombreGrupo;

	@Column(length = 5000)
	private String descripcion;

	@Column(nullable = false, length = 20)
	private String tema;

	@Column(length = 20)
	private String categoriaOtro;

	@ElementCollection
	@CollectionTable(name = "group_rules", joinColumns = @JoinColumn(name = "group_id"))
	@Column(name = "regla")
	private List<String> reglas;

	@Column(nullable = false, length = 10)
	private String privacidad;

	@Column(nullable = false)
	private Long adminId;

	// Getters y setters

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }

	public String getNombreGrupo() { return nombreGrupo; }
	public void setNombreGrupo(String nombreGrupo) { this.nombreGrupo = nombreGrupo; }

	public String getDescripcion() { return descripcion; }
	public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

	public String getTema() { return tema; }
	public void setTema(String tema) { this.tema = tema; }

	public String getCategoriaOtro() { return categoriaOtro; }
	public void setCategoriaOtro(String categoriaOtro) { this.categoriaOtro = categoriaOtro; }

	public List<String> getReglas() { return reglas; }
	public void setReglas(List<String> reglas) { this.reglas = reglas; }

	public String getPrivacidad() { return privacidad; }
	public void setPrivacidad(String privacidad) { this.privacidad = privacidad; }

	public Long getAdminId() { return adminId; }
	public void setAdminId(Long adminId) { this.adminId = adminId; }
}
