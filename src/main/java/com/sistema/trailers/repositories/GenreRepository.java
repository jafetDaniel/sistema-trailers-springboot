package com.sistema.trailers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.trailers.models.Genre;

public interface GenreRepository extends JpaRepository<Genre, Integer>{

}
