package com.ricardoproject.literalura.model;

import jakarta.persistence.*;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "autor")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String nombreAutor;
    private Integer fechaDeNacimiento;
    private Integer fechaDeFallecimiento;
    @OneToMany(mappedBy = "autor", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Libro> libro = new HashSet<>();

    public Autor(){}

    public Autor(DatosAutor datosAutor){
        this.nombreAutor = datosAutor.nombreAutor();
        this.fechaDeNacimiento = datosAutor.fechaDeNacimiento();
        this.fechaDeFallecimiento = datosAutor.fechaDeFallecimiento();
    }

    @Override
    public String toString() {
        return "Autor " +
                "nombreAutor = '" + nombreAutor + '\'' +
                ", fechaDeNacimiento = " + fechaDeNacimiento +
                ", fechaDeFallecimiento = " + fechaDeFallecimiento +
                "Libros: " + (libro != null ?libro.stream()
                .map(Libro::getTitulo)
                .collect(Collectors.joining(", ")) : "N/A") +'\n' +
                '\n';
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

    public Integer getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Integer fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public Integer getFechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }

    public void setFechaDeFallecimiento(Integer fechaDeFallecimiento) {
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }


    public Set<Libro> getLibro() {
        return libro;
    }

    public void setLibro(Set<Libro> libro) {
        this.libro = libro;
        for (Libro libro1 : libro){
            libro1.setAutor(this);
        }
    }
}
