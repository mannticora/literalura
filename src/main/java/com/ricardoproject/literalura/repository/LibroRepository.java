package com.ricardoproject.literalura.repository;

import com.ricardoproject.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro,Long> {
    Libro findByTituloContainsIgnoreCase(String tituloLibro);
    List<Libro> findByIdioma(String idioma);

}
