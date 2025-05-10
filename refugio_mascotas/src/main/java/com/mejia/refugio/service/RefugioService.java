package com.mejia.refugio.service;

import java.util.Date;
import java.util.List;

import com.mejia.refugio.dao.AdopcionDao;
import com.mejia.refugio.dao.MascotaDao;
import com.mejia.refugio.dto.ResultadoPaginado;
import com.mejia.refugio.model.Adopcion;
import com.mejia.refugio.model.Mascota;

public class RefugioService {

    private MascotaDao mascotaDao;
    private AdopcionDao adopcionDao;

    public void setMascotaDao(MascotaDao mascotaDao) {
        this.mascotaDao = mascotaDao;
    }

    public void setAdopcionDao(AdopcionDao adopcionDao) {
        this.adopcionDao = adopcionDao;
    }

    public void registrarMascota(Mascota mascota) {
        if (mascota == null) {
            throw new IllegalArgumentException("La mascota no puede ser nula");
        }
        mascotaDao.crear(mascota);
    }

    public List<Mascota> obtenerTodasMascotas() {
        return mascotaDao.buscarTodos();
    }

    public List<Mascota> buscarMascotasPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        return mascotaDao.buscarPorNombre(nombre);
    }

    public List<Mascota> buscarMascotasPorEdad(int edad) {
        if (edad <= 0) {
            throw new IllegalArgumentException("La edad debe ser un valor positivo");
        }
        return mascotaDao.buscarPorEdad(edad);
    }

    public List<Mascota> buscarMascotasDisponibles() {
        return mascotaDao.buscarDisponiblesParaAdopcion();
    }

    public void registrarAdopcion(Adopcion adopcion) {
        if (adopcion == null) {
            throw new IllegalArgumentException("La adopción no puede ser nula");
        }
        if (adopcion.getMascota() == null || adopcion.getAdoptante() == null) {
            throw new IllegalArgumentException("La adopción debe tener mascota y adoptante asociados");
        }
        adopcionDao.crear(adopcion);
    }

    public List<Adopcion> obtenerAdopcionesActivas() {
        return adopcionDao.buscarAdopcionesActivas();
    }

    public List<Adopcion> obtenerAdopcionesPorAdoptante(Long adoptanteId) {
        if (adoptanteId == null || adoptanteId <= 0) {
            throw new IllegalArgumentException("ID de adoptante inválido");
        }
        return adopcionDao.buscarPorAdoptante(adoptanteId);
    }

    public List<Adopcion> obtenerAdopcionesPorFecha(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");
        }
        if (fechaInicio.after(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha fin");
        }
        return adopcionDao.buscarPorFechaAdopcion(fechaInicio, fechaFin);
    }

    public ResultadoPaginado<Mascota> obtenerMascotasPaginadas(int pagina, int tamanioPagina) {
        validarParametrosPaginacion(pagina, tamanioPagina);

        List<Mascota> mascotas = mascotaDao.buscarPaginados(pagina, tamanioPagina);
        long total = mascotaDao.contarTotalMascotas();

        return new ResultadoPaginado<>(mascotas, pagina, tamanioPagina, total);
    }

    private void validarParametrosPaginacion(int pagina, int tamanioPagina) {
        if (pagina <= 0) {
            throw new IllegalArgumentException("El número de página debe ser mayor a 0");
        }
        if (tamanioPagina <= 0) {
            throw new IllegalArgumentException("El tamaño de página debe ser mayor a 0");
        }
        if (tamanioPagina > 100) {
            throw new IllegalArgumentException("El tamaño máximo de página es 100");
        }
    }
}