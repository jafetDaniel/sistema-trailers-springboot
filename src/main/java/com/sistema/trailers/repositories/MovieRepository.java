package com.sistema.trailers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.trailers.models.Movie;

public interface MovieRepository extends JpaRepository<Movie, Integer>{

}
