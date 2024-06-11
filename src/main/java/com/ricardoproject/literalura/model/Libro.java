package com.ricardoproject.literalura.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")

public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> idioma;
    private Double descarga;
    private String nombreAutor;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id")
    private Autor autor;

    public Libro(){}
    public Libro(DatosLibro datosLibro, Autor autores){
        this.titulo = datosLibro.libro();
        this.idioma = datosLibro.idioma();
        this.descarga = datosLibro.descargas();
        this.nombreAutor = datosLibro.autores()
                .stream().map(DatosAutor::nombreAutor)
                .collect(Collectors.toList()).toString();
        this.autor = autores;
    }

    @Override
    public String toString() {
        return "Libro " +
                ", titulo='" + titulo + '\'' +
                ", idioma=" + idioma +
                ", descarga=" + descarga +
                ", nombreAutor='" + nombreAutor + '\'' +
                ", autor=" + autor;
    }


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }
    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autores) {
        this.autor = autores;
        if (autores != null && !autores.getLibro().contains(this)) {
            autores.getLibro().add(this);
        }
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }


    public List<String> getIdioma() {
        return idioma;
    }

    public void setIdioma(List<String> idioma) {
        this.idioma = idioma;
    }

    public Double getDescarga() {
        return descarga;
    }

    public void setDescarga(Double descarga) {
        this.descarga = descarga;
    }


}