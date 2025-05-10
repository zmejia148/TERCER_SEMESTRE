package com.mejia.refugio.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.mejia.refugio.model.Mascota;

public class MascotaDao {
    private EntityManager em;

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    public void crear(Mascota mascota) {
        em.persist(mascota);
    }

    public List<Mascota> buscarTodos() {
        TypedQuery<Mascota> query = em.createQuery(
            "SELECT m FROM Mascota m ORDER BY m.nombre", Mascota.class);
        return query.getResultList();
    }

    public List<Mascota> buscarPorEdad(int edad) {
        TypedQuery<Mascota> query = em.createQuery(
            "SELECT m FROM Mascota m WHERE m.edad = :edad ORDER BY m.nombre", Mascota.class);
        query.setParameter("edad", edad);
        return query.getResultList();
    }
    
    public List<Mascota> buscarPaginados(int pagina, int tamanioPagina) {
        TypedQuery<Mascota> query = em.createQuery(
            "SELECT m FROM Mascota m ORDER BY m.nombre", Mascota.class);
        query.setFirstResult((pagina - 1) * tamanioPagina);
        query.setMaxResults(tamanioPagina);
        return query.getResultList();
    }

    public long contarTotalMascotas() {
        return em.createQuery(
            "SELECT COUNT(m) FROM Mascota m", Long.class)
            .getSingleResult();
    }

    public List<Mascota> buscarPorNombre(String nombre) {
        TypedQuery<Mascota> query = em.createQuery(
            "SELECT m FROM Mascota m WHERE m.nombre LIKE :nombre ORDER BY m.nombre", Mascota.class);
        query.setParameter("nombre", "%" + nombre + "%");
        return query.getResultList();
    }
    
    public List<Mascota> buscarDisponiblesParaAdopcion() {
        TypedQuery<Mascota> query = em.createQuery(
            "SELECT m FROM Mascota m WHERE m NOT IN " +
            "(SELECT a.mascota FROM Adopcion a WHERE a.fechaDevolucion IS NULL) " +
            "ORDER BY m.nombre", 
            Mascota.class);
        return query.getResultList();
    }

    public List<Mascota> buscarPaginadosConFiltros(int pagina, int tamanioPagina, 
                                                 String nombre, Integer edad, Boolean disponible) {
        StringBuilder jpql = new StringBuilder("SELECT m FROM Mascota m WHERE 1=1");
        
        if (nombre != null && !nombre.isEmpty()) {
            jpql.append(" AND m.nombre LIKE :nombre");
        }
        if (edad != null) {
            jpql.append(" AND m.edad = :edad");
        }
        if (disponible != null) {
            jpql.append(disponible ? 
                " AND m NOT IN (SELECT a.mascota FROM Adopcion a WHERE a.fechaDevolucion IS NULL)" :
                " AND m IN (SELECT a.mascota FROM Adopcion a WHERE a.fechaDevolucion IS NULL)");
        }
        
        jpql.append(" ORDER BY m.nombre");
        
        TypedQuery<Mascota> query = em.createQuery(jpql.toString(), Mascota.class);
        
        if (nombre != null && !nombre.isEmpty()) {
            query.setParameter("nombre", "%" + nombre + "%");
        }
        if (edad != null) {
            query.setParameter("edad", edad);
        }
        
        query.setFirstResult((pagina - 1) * tamanioPagina);
        query.setMaxResults(tamanioPagina);
        
        return query.getResultList();
    }

    public long contarConFiltros(String nombre, Integer edad, Boolean disponible) {
        StringBuilder jpql = new StringBuilder("SELECT COUNT(m) FROM Mascota m WHERE 1=1");
        
        if (nombre != null && !nombre.isEmpty()) {
            jpql.append(" AND m.nombre LIKE :nombre");
        }
        if (edad != null) {
            jpql.append(" AND m.edad = :edad");
        }
        if (disponible != null) {
            jpql.append(disponible ? 
                " AND m NOT IN (SELECT a.mascota FROM Adopcion a WHERE a.fechaDevolucion IS NULL)" :
                " AND m IN (SELECT a.mascota FROM Adopcion a WHERE a.fechaDevolucion IS NULL)");
        }
        
        TypedQuery<Long> query = em.createQuery(jpql.toString(), Long.class);
        
        if (nombre != null && !nombre.isEmpty()) {
            query.setParameter("nombre", "%" + nombre + "%");
        }
        if (edad != null) {
            query.setParameter("edad", edad);
        }
        
        return query.getSingleResult();
    }

    public List<Mascota> buscarPorEspecie(Long especieId) {
        TypedQuery<Mascota> query = em.createQuery(
            "SELECT m FROM Mascota m WHERE m.especie.id = :especieId ORDER BY m.nombre", 
            Mascota.class);
        query.setParameter("especieId", especieId);
        return query.getResultList();
    }
}