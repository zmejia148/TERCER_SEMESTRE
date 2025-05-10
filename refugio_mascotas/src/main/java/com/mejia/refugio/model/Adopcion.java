package com.mejia.refugio.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Adopcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Temporal(TemporalType.DATE)
    private Date fechaAdopcion;
    
    @Temporal(TemporalType.DATE)
    private Date fechaDevolucion;
    
    @ManyToOne
    @JoinColumn(name = "mascota_id")
    private Mascota mascota;
    
    @ManyToOne
    @JoinColumn(name = "adoptante_id")
    private Adoptante adoptante;
    
    public Adopcion() {}
    
    public Adopcion(Date fechaAdopcion, Mascota mascota, Adoptante adoptante) {
        this.fechaAdopcion = fechaAdopcion;
        this.mascota = mascota;
        this.adoptante = adoptante;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Date getFechaAdopcion() { return fechaAdopcion; }
    public void setFechaAdopcion(Date fechaAdopcion) { this.fechaAdopcion = fechaAdopcion; }
    public Date getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(Date fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }
    public Mascota getMascota() { return mascota; }
    public void setMascota(Mascota mascota) { this.mascota = mascota; }
    public Adoptante getAdoptante() { return adoptante; }
    public void setAdoptante(Adoptante adoptante) { this.adoptante = adoptante; }
}