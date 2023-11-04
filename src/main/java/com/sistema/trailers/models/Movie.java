package com.sistema.trailers.models;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "movies")
public class Movie {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@NotBlank
	private String title;
	
	@NotBlank
	private String synopsis;
	
	@Column(name = "date_release")
	@NotNull
	@DateTimeFormat(iso= ISO.DATE)
	private LocalDate dateRelease;
	
	@Column(name = "url_trailer")
	@NotBlank
	private String urlTrailer;
	
	@Column(name = "route_image")
	private String routeImage;
	
	@NotEmpty
	@ManyToMany(fetch = FetchType.LAZY)//lazy:carga solo cuando sea necesario
	@JoinTable(name = "genres_movies", joinColumns = @JoinColumn(name = "id_movie"), inverseJoinColumns = @JoinColumn(name = "id_genre"))
	private List<Genre> genres;
	
	@Transient
	private MultipartFile image;

	public Movie() {
	}
	
	public Movie(Integer id, @NotBlank String title, @NotBlank String synopsis, @NotNull LocalDate dateRelease,
			@NotBlank String urlTrailer, String routeImage, @NotEmpty List<Genre> genres, MultipartFile image) {
		this.id = id;
		this.title = title;
		this.synopsis = synopsis;
		this.dateRelease = dateRelease;
		this.urlTrailer = urlTrailer;
		this.routeImage = routeImage;
		this.genres = genres;
		this.image = image;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public LocalDate getDateRelease() {
		return dateRelease;
	}

	public void setDateRelease(LocalDate dateRelease) {
		this.dateRelease = dateRelease;
	}

	public String getUrlTrailer() {
		return urlTrailer;
	}

	public void setUrlTrailer(String urlTrailer) {
		this.urlTrailer = urlTrailer;
	}

	public String getRouteImage() {
		return routeImage;
	}

	public void setRouteImage(String routeImage) {
		this.routeImage = routeImage;
	}

	public List<Genre> getGenres() {
		return genres;
	}

	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}
	
	
	
}
