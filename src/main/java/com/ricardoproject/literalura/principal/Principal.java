package com.ricardoproject.literalura.principal;

import com.ricardoproject.literalura.model.Datos;
import com.ricardoproject.literalura.model.DatosAutor;
import com.ricardoproject.literalura.model.DatosLibro;
import com.ricardoproject.literalura.service.ConsumoAPI;
import com.ricardoproject.literalura.service.ConvierteDatos;

import java.util.*;

public class Principal {
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private final String SEARCH_COMPLEMENT = "%20";
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<Datos> datosLibros = new ArrayList<>();

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                     ----------------
                     Elija una opción para la consulta en la base de datos:
                     1 - Buscar libro por título
                     2 - Listar libros registrados
                     3 - Listar autores registrados
                     4 - Listar autores vivos en un determinado año
                     5 - Listar libros por idioma
                     0 - Salir
                     ----------------
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    mostrarLibrosBuscados();
                    break;
                case 3:
                    mostrarAutoresBuscados();
                    break;
                case 4:
                    mostrarAutoresVivosPorAño();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicaión...");
                    break;
                default:
                    System.out.println("Opción invalida");
            }
        }
    }

    private Datos getDatosLibro() {
        System.out.println("Escribe el nombre del libro que deseas buscar:\n");
        var nombreLibro = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE +
                nombreLibro.replace(" ", "+") +
                SEARCH_COMPLEMENT);
        Datos datos = conversor.obtenerDatos(json, Datos.class);
        datos.resultados().stream()
                .sorted(Comparator.comparing(DatosLibro::libro))
                .limit(1)
                .forEach(this::imprimirDatosLibros);
        return datos;
    }

    private void buscarLibroPorTitulo() {
        Datos datosLibro = getDatosLibro();
        datosLibros.add(datosLibro);
    }

    private void mostrarLibrosBuscados() {
        for (Datos datos : datosLibros) {
            datos.resultados()
                    .stream().limit(1)
            .forEach(this::imprimirDatosLibros);
        }
    }

    private void imprimirDatosLibros(DatosLibro libro) {
        StringBuilder sb = new StringBuilder();

        sb.append("Título: ").append(libro.libro()).append("\n");

        sb.append("Autores: ");
        for (DatosAutor autor : libro.autores()) {
            sb.append(autor.nombreAutor());
            if (autor.fechaDeNacimiento() != null) {
                sb.append("\nFecha de nacimiento: ").append(autor.fechaDeNacimiento());
            }
            sb.append("\n");
        }

        sb.append("Idiomas: ").append(String.join(", ", libro.idioma())).append("\n");
        sb.append("Descargas: ").append(libro.descargas()).append("\n");

        System.out.println(sb.toString());
    }

    private void mostrarAutoresBuscados() {
        datosLibros.stream()
                .flatMap(d -> d.resultados().stream())
                .flatMap(l -> l.autores().stream())
                .forEach(a -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Autor: ").append(a.nombreAutor()).append("\n");

                    if (a.fechaDeNacimiento() != null) {
                        sb.append("Fecha de nacimiento: ").append(a.fechaDeNacimiento()).append("\n");
                    } else {
                        sb.append("Fecha de nacimiento: No disponible\n");
                    }

                    if (a.fechaDeFallecimiento() != null) {
                        sb.append("Fecha de fallecimiento: ").append(a.fechaDeFallecimiento()).append("\n");
                    } else {
                        sb.append("Fecha de fallecimiento: No disponible\n");
                    }

                    System.out.println(sb.toString());
                });
    }


    private void mostrarAutoresVivosPorAño() {
        System.out.println("Escribe el año que deseas consultar:\n");
        int año = teclado.nextInt();
        teclado.nextLine();  // Consume el salto de línea

        datosLibros.stream()
                .flatMap(datos -> datos.resultados().stream())
                .filter(libro -> libro.autores().stream()
                        .anyMatch(autor -> autorEstabaVivoEnAño(autor, año)))
                .limit(1)
                .forEach(libro -> imprimirDatosLibros(libro));
    }

    private boolean autorEstabaVivoEnAño(DatosAutor autor, int año) {
        if (autor.fechaDeNacimiento() != null && autor.fechaDeNacimiento() > año) {
            return false; // El autor no había nacido aún
        }
        return autor.fechaDeFallecimiento() == null || autor.fechaDeFallecimiento() >= año;
    }


    private void listarLibrosPorIdioma() {
        System.out.println("""
                Ingrese el idioma para buscar los libros:
                es - español
                en - inglés
                fr - francés
                pt - portugués
                """);
        String idiomaDeseado = teclado.nextLine();

        List<DatosLibro> librosFiltrados = datosLibros.stream()
                .flatMap(datos -> datos.resultados().stream())
                .filter(libro -> libro.idioma().stream()
                        .anyMatch(idioma -> idioma.equalsIgnoreCase(idiomaDeseado)))
                .toList();

        if (librosFiltrados.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma especificado.");
        } else {
            librosFiltrados.forEach(this::imprimirDatosLibros);
        }
    }

}