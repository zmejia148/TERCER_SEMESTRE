package com.mejia.refugio.dto;

import java.util.List;

public class ResultadoPaginado<T> {
    private List<T> elementos;
    private int paginaActual;
    private int totalPaginas;
    private long totalElementos;
    private int elementosPorPagina;

    public ResultadoPaginado(List<T> elementos, int paginaActual, 
                           int elementosPorPagina, long totalElementos) {
        this.elementos = elementos;
        this.paginaActual = paginaActual;
        this.elementosPorPagina = elementosPorPagina;
        this.totalElementos = totalElementos;
        this.totalPaginas = (int) Math.ceil((double) totalElementos / elementosPorPagina);
    }

    // Getters
    public List<T> getElementos() { return elementos; }
    public int getPaginaActual() { return paginaActual; }
    public int getTotalPaginas() { return totalPaginas; }
    public long getTotalElementos() { return totalElementos; }
    public int getElementosPorPagina() { return elementosPorPagina; }
    
    // Métodos útiles
    public boolean tieneAnterior() { return paginaActual > 1; }
    public boolean tieneSiguiente() { return paginaActual < totalPaginas; }
}