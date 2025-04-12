package com.mycompany.ejemplo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "alumnos") // Nombre de la tabla en la base de datos

public class Ejemplo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática del ID
    private int id;

    @Column(nullable = false, length = 50) // Campo obligatorio con longitud máxima
    private String nombre;

    @Column(nullable = false, length = 50)
    private String direccion;

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return direccion;
    }

    public void setApellido(String apellido) {
        this.direccion = direccion;
    }
}
