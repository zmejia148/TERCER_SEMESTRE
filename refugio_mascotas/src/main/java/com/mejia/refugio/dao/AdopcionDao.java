package com.mejia.refugio.dao;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import com.mejia.refugio.model.Adopcion;

public class AdopcionDao {
    private EntityManager em;

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
    
    public void crear(Adopcion adopcion) {
        em.persist(adopcion);
    }
    
    public List<Adopcion> buscarAdopcionesActivas() {
        TypedQuery<Adopcion> query = em.createQuery(
            "SELECT a FROM Adopcion a WHERE a.fechaDevolucion IS NULL", Adopcion.class);
        return query.getResultList();
    }
    
    public List<Adopcion> buscarPorAdoptante(Long adoptanteId) {
        TypedQuery<Adopcion> query = em.createQuery(
            "SELECT a FROM Adopcion a WHERE a.adoptante.id = :adoptanteId", Adopcion.class);
        query.setParameter("adoptanteId", adoptanteId);
        return query.getResultList();
    }
    
    public List<Adopcion> buscarPorFechaAdopcion(Date fechaInicio, Date fechaFin) {
        TypedQuery<Adopcion> query = em.createQuery(
            "SELECT a FROM Adopcion a WHERE a.fechaAdopcion BETWEEN :fechaInicio AND :fechaFin " +
            "ORDER BY a.fechaAdopcion DESC", Adopcion.class);
        query.setParameter("fechaInicio", fechaInicio);
        query.setParameter("fechaFin", fechaFin);
        return query.getResultList();
    }
}