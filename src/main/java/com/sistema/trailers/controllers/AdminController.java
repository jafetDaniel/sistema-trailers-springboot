package com.sistema.trailers.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sistema.trailers.models.Genre;
import com.sistema.trailers.models.Movie;
import com.sistema.trailers.repositories.GenreRepository;
import com.sistema.trailers.repositories.MovieRepository;
import com.sistema.trailers.service.AlmacenServiceImpl;
import com.sistema.trailers.util.paginator.PageRender;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private GenreRepository genreRepository;
	
	@Autowired
	private AlmacenServiceImpl service;
	
	@GetMapping("")
	public ModelAndView verPaginaInicio(@RequestParam(name="page", defaultValue="0") int page){

		Pageable pageRequest = PageRequest.of(page, 5); //objeto de tipo PageRequest, indicando la pagina y cant de registros
		Page<Movie> movies = movieRepository.findAll(pageRequest); //obtener consulta a BD a partir de la pagina
		PageRender<Movie> pageRender = new PageRender<>("", movies);
		
		return new ModelAndView("admin/index").addObject("movies", movies)
				                              .addObject("titulo", "Administración")
				                              .addObject("page", pageRender);
	}
	
	@GetMapping("/peliculas/nuevo")
	public ModelAndView mostrarFormNuevoPelicula(@PageableDefault(sort = "title", size = 5)Pageable pageable){
		
		List<Genre> genres = genreRepository.findAll(Sort.by("title"));
		
		return new ModelAndView("admin/form-pelicula").addObject("movie", new Movie())
				                                       .addObject("genres", genres)
				                                       .addObject("titulo", "Nueva Película");
	}
	
	@PostMapping("/peliculas/nuevo")
	public ModelAndView registrarPelicula(@Validated Movie movie,BindingResult bindingResult) {
		
		if(bindingResult.hasErrors() || movie.getImage().isEmpty()) {
			if(movie.getImage().isEmpty()) {
				bindingResult.rejectValue("image","MultipartNotEmpty");
			}
			
			List<Genre> genres = genreRepository.findAll(Sort.by("title"));
			return new ModelAndView("admin/form-pelicula")
					.addObject("movie", movie)
					.addObject("genres",genres);
		}
		
		String rutaImage = service.almacenarArchivo(movie.getImage());
		movie.setRouteImage(rutaImage);
		
		movieRepository.save(movie);//guardar en bd
		return new ModelAndView("redirect:/admin");
	}
	
	@GetMapping("/peliculas/{id}/editar")
	public ModelAndView mostrarFormularioDeEditarPelicula(@PathVariable Integer id) {
		
		Movie movie = movieRepository.getOne(id);
		List<Genre> generos = genreRepository.findAll(Sort.by("title"));
		
		return new ModelAndView("admin/form-pelicula")
				.addObject("movie",movie)
				.addObject("genres",generos)
				.addObject("titulo", "Editar Película");
	}
	
	
	@PostMapping("/peliculas/{id}/editar")
	public ModelAndView actualizarPelicula(@PathVariable Integer id, @Validated Movie movie, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			List<Genre> genres = genreRepository.findAll(Sort.by("title"));
			return new ModelAndView("admin/form-pelicula")
					.addObject("movie",movie)
					.addObject("genres",genres);
		}
		
		Movie peliculaDB = movieRepository.getOne(id);
		peliculaDB.setTitle(movie.getTitle());
		peliculaDB.setSynopsis(movie.getSynopsis());
		peliculaDB.setDateRelease(movie.getDateRelease());
		peliculaDB.setUrlTrailer(movie.getUrlTrailer());
		peliculaDB.setGenres(movie.getGenres());
		
		if(!movie.getImage().isEmpty()) {
			service.deleteArchivo(peliculaDB.getRouteImage());
			String rutaPortada = service.almacenarArchivo(movie.getImage());
			peliculaDB.setRouteImage(rutaPortada);
		}
		
		movieRepository.save(peliculaDB);
		return new ModelAndView("redirect:/admin");
	}
	
	@GetMapping("/peliculas/{id}/eliminar")
	public String eliminarPelicula(@PathVariable Integer id) {
		Movie movie = movieRepository.getOne(id);
		movieRepository.delete(movie);
		service.deleteArchivo(movie.getRouteImage());
		
		return "redirect:/admin";
	}
	

	@GetMapping("/generos")
	public ModelAndView verPaginaGeneros(@RequestParam(name="page", defaultValue="0") int page){

		Pageable pageRequest = PageRequest.of(page, 10); //objeto de tipo PageRequest, indicando la pagina y cant de registros
		Page<Genre> genres = genreRepository.findAll(pageRequest); //obtener consulta a BD a partir de la pagina
		PageRender<Genre> pageRender = new PageRender<>("", genres);
		
		return new ModelAndView("admin/generos").addObject("genres", genres)
				                              .addObject("titulo", "Géneros")
				                              .addObject("page", pageRender);
	}
	
	@GetMapping("/generos/nuevo")
	public ModelAndView mostrarFormNuevoGenero(@PageableDefault(sort = "title", size = 5)Pageable pageable){

		return new ModelAndView("admin/form-genero").addObject("genre", new Genre())
				                                       .addObject("titulo", "Nuevo Género");
	}
	
	@PostMapping("/generos/nuevo")
	public ModelAndView registrarGenero(@Validated Genre genre,BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			
			return new ModelAndView("admin/form-genero")
					.addObject("genre",genre)
					.addObject("titulo", "Nuevo Género");
		}	
		genreRepository.save(genre);//guardar en bd
		return new ModelAndView("redirect:/admin/generos");
	}
	
	@GetMapping("/generos/{id}/editar")
	public ModelAndView mostrarFormularioDeEditarGenero(@PathVariable Integer id) {
		
		Genre genre = genreRepository.getOne(id);
		
		return new ModelAndView("admin/form-genero")
				.addObject("genre",genre)
				.addObject("titulo", "Editar Género");
	}
	
	
	@PostMapping("/generos/{id}/editar")
	public ModelAndView actualizarGenero(@PathVariable Integer id, @Validated Genre genre, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			return new ModelAndView("admin/form-genero")
					.addObject("genre",genre);
		}
		
		Genre genreDB = genreRepository.getOne(id);
		genreDB.setTitle(genre.getTitle());
		
		genreRepository.save(genreDB);
		return new ModelAndView("redirect:/admin/generos");
	}
	
	@GetMapping("/generos/{id}/eliminar")
	public String eliminarGenero(@PathVariable Integer id) {
		Genre genre = genreRepository.getOne(id);
		genreRepository.delete(genre);
		
		return "redirect:/admin/generos";
	}
	

}
