package com.communityapp.group.model;

import jakarta.persistence.*;

@Entity
@Table(name = "groups")
public class Group {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_grupo", nullable = false, length = 60)
    private String nombreGrupo;

    @Column(length = 5000)
    private String descripcion;

    @Column(nullable = false, length = 50)
    private String categoria;

    @Column(name = "categoria_otro", length = 50)
    private String categoriaOtro;

    @Column(nullable = false, length = 10)
    private String privacidad;

    @Column(name = "acepta_reglas", nullable = false)
    private boolean aceptaReglas;

    @Column(name = "admin_id", nullable = false)
    private Long adminId;

    @Column(nullable = false, length = 50)
    private String tema;

    // Getters y Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreGrupo() { return nombreGrupo; }
    public void setNombreGrupo(String nombreGrupo) { this.nombreGrupo = nombreGrupo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getCategoriaOtro() { return categoriaOtro; }
    public void setCategoriaOtro(String categoriaOtro) { this.categoriaOtro = categoriaOtro; }

    public String getPrivacidad() { return privacidad; }
    public void setPrivacidad(String privacidad) { this.privacidad = privacidad; }

    public boolean isAceptaReglas() { return aceptaReglas; }
    public void setAceptaReglas(boolean aceptaReglas) { this.aceptaReglas = aceptaReglas; }

    public String getTema() { return tema; }
    public void setTema(String tema) { this.tema = tema; }

    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }
}
