package com.ricardoproject.literalura.repository;

import com.ricardoproject.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    Autor findByNombreAutor (String nombreAutor);
    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento <= :año AND a.fechaDeFallecimiento >= :año")
    List<Autor> autoresVivosEnDeterminadoAño (Integer año);

}
