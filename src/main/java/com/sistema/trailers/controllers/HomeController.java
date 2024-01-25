package com.sistema.trailers.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sistema.trailers.models.Movie;
import com.sistema.trailers.repositories.MovieRepository;
import com.sistema.trailers.util.paginator.PageRender;

@Controller
@RequestMapping("")
public class HomeController {
	
	 @Autowired
	 private MovieRepository movieRepository;
	
	@GetMapping({"","/","/index"})
	public ModelAndView verPaginaDeInicio() {
		List<Movie> ultimasPeliculas = movieRepository.findAll(PageRequest.of(0,4,Sort.by("dateRelease").descending())).toList();
		return new ModelAndView("index")
				      .addObject("ultimasPeliculas", ultimasPeliculas)
				      .addObject("titulo","Inicio");
	}

	@GetMapping("/peliculas")
	public ModelAndView listarPeliculas(@RequestParam(name="page", defaultValue="0") int page) {
		
		Pageable pageRequest = PageRequest.of(page, 10); //objeto de tipo PageRequest, indicando la pagina y cant de registros
		Page<Movie> movies = movieRepository.findAll(pageRequest); //obtener consulta a BD a partir de la pagina
		PageRender<Movie> pageRender = new PageRender<>("", movies);
		
		return new ModelAndView("peliculas")
				        .addObject("movies",movies)
				        .addObject("titulo", "Películas")
				        .addObject("page", pageRender);
	}
	
	@GetMapping("/peliculas/{id}")
	public ModelAndView mostrarDetallesDePelicula(@PathVariable Integer id) {
		Movie movie = movieRepository.getOne(id);
		return new ModelAndView("pelicula").addObject("movie",movie).addObject("titulo", movie.getTitle());
	}

}
