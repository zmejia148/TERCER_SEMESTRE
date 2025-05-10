package com.mejia.refugio.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Mascota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private int edad;
    private String descripcion;
    private boolean esterilizado;
    
    @ManyToOne
    @JoinColumn(name = "especie_id")
    private Especie especie;
    
    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Adopcion> adopciones = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "refugio_id")
    private Refugio refugio;

    public Mascota() {}
    
    public Mascota(String nombre, int edad, String descripcion, boolean esterilizado) {
        this.nombre = nombre;
        this.edad = edad;
        this.descripcion = descripcion;
        this.esterilizado = esterilizado;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public boolean isEsterilizado() { return esterilizado; }
    public void setEsterilizado(boolean esterilizado) { this.esterilizado = esterilizado; }
    public Especie getEspecie() { return especie; }
    public void setEspecie(Especie especie) { this.especie = especie; }
    public List<Adopcion> getAdopciones() { return adopciones; }
    public void setAdopciones(List<Adopcion> adopciones) { this.adopciones = adopciones; }
    public Refugio getRefugio() { return refugio; }
    public void setRefugio(Refugio refugio) { this.refugio = refugio; }
}