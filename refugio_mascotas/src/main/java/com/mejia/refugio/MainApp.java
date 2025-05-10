package com.mejia.refugio;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.mejia.refugio.dao.AdopcionDao;
import com.mejia.refugio.dao.MascotaDao;
import com.mejia.refugio.dto.ResultadoPaginado;
import com.mejia.refugio.model.Adopcion;
import com.mejia.refugio.model.Adoptante;
import com.mejia.refugio.model.Especie;
import com.mejia.refugio.model.Mascota;
import com.mejia.refugio.model.Refugio;
import com.mejia.refugio.service.RefugioService;

public class MainApp {
    private static RefugioService refugioService;
    private static Scanner scanner;

    static {
        // Configuración de encoding al cargar la clase
        System.setProperty("file.encoding", "UTF-8");
        try {
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8.name()));
        } catch (UnsupportedEncodingException e) {
            System.err.println("Error configurando encoding UTF-8");
        }
    }

    public static void main(String[] args) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("refugioPU");
            EntityManager em = emf.createEntityManager();
            scanner = new Scanner(System.in, StandardCharsets.UTF_8.name());

            try {
                inicializarDatos(em);
                refugioService = configurarServicios(em);
                mostrarMenuPrincipal();
            } catch (Exception e) {
                manejarError(em, e);
            } finally {
                cerrarRecursos(em, emf);
            }
        } catch (Exception e) {
            System.err.println("Error inicial: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void inicializarDatos(EntityManager em) {
        em.getTransaction().begin();

        Refugio refugioCentral = new Refugio("Refugio Central", "Calle Principal 123");
        em.persist(refugioCentral);

        // Crear especies
        Especie perro = new Especie("Perro", "Canino doméstico");
        Especie gato = new Especie("Gato", "Felino doméstico");
        Especie conejo = new Especie("Conejo", "Conejo común");
        em.persist(perro);
        em.persist(gato);
        em.persist(conejo);

        // Crear mascotas
        Mascota mascota1 = crearMascota("Firulais", 3, "Le gusta comer hamburguesas", true, perro, refugioCentral);
        Mascota mascota2 = crearMascota("Michi", 2, "Tranquilo y mimoso", true, gato, refugioCentral);
        Mascota mascota3 = crearMascota("Bad Bunny", 3, "Le gusta la musica", false, conejo, refugioCentral);
        em.persist(mascota1);
        em.persist(mascota2);
        em.persist(mascota3);

        // Crear adoptantes y adopciones
        crearAdopciones(em, refugioCentral, mascota1, mascota2, mascota3);

        em.getTransaction().commit();
    }

    private static Mascota crearMascota(String nombre, int edad, String descripcion, boolean esterilizado,
            Especie especie, Refugio refugio) {
        Mascota mascota = new Mascota(nombre, edad, descripcion, esterilizado);
        mascota.setEspecie(especie);
        mascota.setRefugio(refugio);
        return mascota;
    }

    private static void crearAdopciones(EntityManager em, Refugio refugio, Mascota... mascotas) {
        Adoptante adoptante1 = new Adoptante("Juan Castro", "0999999999", "Av. Siempre Viva 742");
        Adoptante adoptante2 = new Adoptante("María Perez", "0988888888", "Calle Principal 123");
        Adoptante adoptante3 = new Adoptante("Benito Martínez", "0977777777", "Calle Principal 421");

        adoptante1.setRefugio(refugio);
        adoptante2.setRefugio(refugio);
        adoptante3.setRefugio(refugio);

        em.persist(adoptante1);
        em.persist(adoptante2);
        em.persist(adoptante3);

        em.persist(new Adopcion(new Date(), mascotas[0], adoptante1));
        em.persist(new Adopcion(new Date(), mascotas[1], adoptante2));
        em.persist(new Adopcion(new Date(), mascotas[2], adoptante3));
    }

    private static RefugioService configurarServicios(EntityManager em) {
        MascotaDao mascotaDao = new MascotaDao();
        mascotaDao.setEntityManager(em);

        AdopcionDao adopcionDao = new AdopcionDao();
        adopcionDao.setEntityManager(em);

        RefugioService service = new RefugioService();
        service.setMascotaDao(mascotaDao);
        service.setAdopcionDao(adopcionDao);
        return service;
    }

    private static void mostrarMenuPrincipal() {
        boolean salir = false;

        while (!salir) {
            printSeparator();
            System.out.println("REFUGIO DE MASCOTAS - MENÚ PRINCIPAL");
            printSeparator();
            System.out.println("1. Ver todas las mascotas");
            System.out.println("2. Ver mascotas disponibles");
            System.out.println("3. Ver adopciones activas");
            System.out.println("4. Navegar mascotas (paginado)");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    mostrarTodasMascotas();
                    break;
                case "2":
                    mostrarMascotasDisponibles();
                    break;
                case "3":
                    mostrarAdopcionesActivas();
                    break;
                case "4":
                    navegarMascotasPaginadas();
                    break;
                case "0":
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }

            if (!salir) {
                System.out.print("\nPresione Enter para continuar...");
                scanner.nextLine();
            }
        }
    }

    private static void mostrarTodasMascotas() {
        printSectionHeader("Todas las mascotas registradas");
        List<Mascota> mascotas = refugioService.obtenerTodasMascotas();
        imprimirMascotas(mascotas);
    }

    private static void mostrarMascotasDisponibles() {
        printSectionHeader("Mascotas disponibles para adopción");
        List<Mascota> mascotas = refugioService.buscarMascotasDisponibles();
        if (mascotas.isEmpty()) {
            System.out.println("No hay mascotas disponibles en este momento.");
        } else {
            imprimirMascotas(mascotas);
        }
    }

    private static void mostrarAdopcionesActivas() {
        printSectionHeader("Adopciones activas");
        List<Adopcion> adopciones = refugioService.obtenerAdopcionesActivas();
        if (adopciones.isEmpty()) {
            System.out.println("No hay adopciones activas actualmente.");
        } else {
            System.out.printf("%-15s %-20s %-15s %-12s%n",
                    "Mascota", "Adoptante", "Fecha", "Estado");
            System.out.println("-----------------------------------------------------");
            adopciones.forEach(a -> System.out.printf("%-15s %-20s %-15s %-12s%n",
                    a.getMascota().getNombre(),
                    a.getAdoptante().getNombre(),
                    a.getFechaAdopcion().toString().substring(0, 10),
                    a.getFechaDevolucion() == null ? "Activa" : "Finalizada"));
        }
    }

    private static void navegarMascotasPaginadas() {
        int pagina = 1;
        int elementosPorPagina = 2;
        boolean volver = false;

        while (!volver) {
            ResultadoPaginado<Mascota> resultado = refugioService.obtenerMascotasPaginadas(pagina, elementosPorPagina);

            printSectionHeader("Mascotas (Página " + pagina + " de " + resultado.getTotalPaginas() + ")");
            System.out.printf("Mostrando %d de %d mascotas\n\n",
                    resultado.getElementos().size(), resultado.getTotalElementos());

            imprimirMascotas(resultado.getElementos());

            System.out.println("\nOpciones de navegación:");
            if (resultado.tieneAnterior()) {
                System.out.println("[A] Página anterior");
            }
            if (resultado.tieneSiguiente()) {
                System.out.println("[S] Página siguiente");
            }
            System.out.println("[0] Volver al menú principal");
            System.out.print("Seleccione: ");

            String opcion = scanner.nextLine().toUpperCase();

            switch (opcion) {
                case "A":
                    if (resultado.tieneAnterior())
                        pagina--;
                    break;
                case "S":
                    if (resultado.tieneSiguiente())
                        pagina++;
                    break;
                case "0":
                    volver = true;
                    break;
                default:
                    System.out.println("Opción no válida");
            }
        }
    }

    private static void imprimirMascotas(List<Mascota> mascotas) {
        System.out.printf("%-20s %-15s %-10s %-30s %-10s%n",
                "Nombre", "Especie", "Edad", "Descripción", "Esterilizado");
        System.out.println("-------------------------------------------------------------------------------");
        mascotas.forEach(m -> System.out.printf("%-20s %-15s %-10d %-30s %-10s%n",
                m.getNombre(),
                m.getEspecie().getNombre(),
                m.getEdad(),
                m.getDescripcion().length() > 25 ? m.getDescripcion().substring(0, 25) + "..." : m.getDescripcion(),
                m.isEsterilizado() ? "Sí" : "No"));
    }

    private static void manejarError(EntityManager em, Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        System.err.println("Error: " + e.getMessage());
        e.printStackTrace();
    }

    private static void cerrarRecursos(EntityManager em, EntityManagerFactory emf) {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
        if (scanner != null) {
            scanner.close();
        }
    }

    private static void printSeparator() {
        System.out.println("\n===========================================\n");
    }

    private static void printSectionHeader(String title) {
        System.out.println("\n" + title.toUpperCase());
        System.out.println("-------------------------------------------");
    }
}