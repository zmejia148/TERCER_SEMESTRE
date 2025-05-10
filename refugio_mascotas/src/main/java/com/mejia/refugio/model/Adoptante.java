package com.mejia.refugio.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Adoptante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private String telefono;
    private String direccion;
    
    @OneToMany(mappedBy = "adoptante")
    private List<Adopcion> adopciones;
    
    @ManyToOne
    @JoinColumn(name = "refugio_id")
    private Refugio refugio;

    public Adoptante() {}
    
    public Adoptante(String nombre, String telefono, String direccion) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public List<Adopcion> getAdopciones() { return adopciones; }
    public void setAdopciones(List<Adopcion> adopciones) { this.adopciones = adopciones; }
    public Refugio getRefugio() { return refugio; }
    public void setRefugio(Refugio refugio) { this.refugio = refugio; }
}