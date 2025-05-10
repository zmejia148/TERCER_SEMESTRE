package com.mejia.refugio.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Refugio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private String direccion;
    
    @OneToMany(mappedBy = "refugio")
    private List<Mascota> mascotas;
    
    @OneToMany(mappedBy = "refugio")
    private List<Adoptante> adoptantes;

    public Refugio() {}
    
    public Refugio(String nombre, String direccion) {
        this.nombre = nombre;
        this.direccion = direccion;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public List<Mascota> getMascotas() { return mascotas; }
    public void setMascotas(List<Mascota> mascotas) { this.mascotas = mascotas; }
    public List<Adoptante> getAdoptantes() { return adoptantes; }
    public void setAdoptantes(List<Adoptante> adoptantes) { this.adoptantes = adoptantes; }
}