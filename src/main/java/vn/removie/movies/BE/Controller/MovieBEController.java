package vn.removie.movies.BE.Controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vn.removie.movies.Entity.EGenre;
import vn.removie.movies.Entity.Genre;
import vn.removie.movies.Entity.Movie;
import vn.removie.movies.Service.GenreService;
import vn.removie.movies.Service.MovieService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/be/movies")
@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
public class MovieBEController {
    @Autowired
    private MovieService movieService;
    @Autowired
    private GenreService genreService;


    private void addPaginatedAttributesToModel(Model model, Page<Movie> moviePage, int currentPage) {
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("moviePage", moviePage);
        model.addAttribute("totalPages", moviePage.getTotalPages());

        List<Integer> pageNumbers = IntStream.rangeClosed(1, moviePage.getTotalPages())
                .boxed()
                .collect(Collectors.toList());
        model.addAttribute("pageNumbers", pageNumbers);
    }
    @GetMapping("/listMovies")
    public String showMovieList(Model model,
                                @RequestParam("page") Optional<Integer> page,
                                @RequestParam("size") Optional<Integer> size,
                                @RequestParam(required = false) String successMessage) {

        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
        }

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(4);

        Page<Movie> moviePage = movieService.findPaginated(
                PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.DESC, "createAt")));

        addPaginatedAttributesToModel(model, moviePage, currentPage);

        return "movie-list";
    }

    @GetMapping("/listMoviesByTitleAsc")
    public String showMovieListByTitleAsc(Model model,
                                          @RequestParam("page") Optional<Integer> page,
                                          @RequestParam("size") Optional<Integer> size,
                                          @RequestParam(required = false) String successMessage) {

        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
        }

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(4);

        Page<Movie> moviePage = movieService.findPaginatedByTitleAsc(
                PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.ASC, "title")));

        addPaginatedAttributesToModel(model, moviePage, currentPage);

        return "movie-list";
    }

    @GetMapping("/listMoviesByTitleDesc")
    public String showMovieListByTitleDesc(Model model,
                                           @RequestParam("page") Optional<Integer> page,
                                           @RequestParam("size") Optional<Integer> size,
                                           @RequestParam(required = false) String successMessage) {

        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
        }

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(4);

        Page<Movie> moviePage = movieService.findPaginatedByTitleDesc(
                PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.DESC, "title")));

        addPaginatedAttributesToModel(model, moviePage, currentPage);

        return "movie-list";
    }

    @GetMapping("/search-by-title")
    public String accountList(@RequestParam(name = "search", required = false) String search,
                              @RequestParam("page") Optional<Integer> page,
                              @RequestParam("size") Optional<Integer> size,
                              Model model) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(4);

        if (search != null && !search.isEmpty()) {
            Page<Movie> moviePage = movieService.findPaginatedByTitle(
                    search, PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.DESC, "createAt")));

            addPaginatedAttributesToModel(model, moviePage, currentPage);

        } else {
            return "redirect:/be/movies/listMovies";
        }
        return "movie-list";
    }


    @PostMapping("/save")
    public String saveMovie(@ModelAttribute Movie movie, Model model) {
        if (movie.isActive()) {
            movie.setImdbId(movie.getImdbId().trim());
            movie.setTitle(movie.getTitle().trim());
            movieService.saveMovie(movie);
            return "redirect:/be/movies/listMovies?successMessage=EDIT%20MOVIE%20SUCCESS";
        } else {
            if (movieService.IsExistMovieImdbId(movie.getImdbId().trim())) {
                model.addAttribute("errorMessage", "IMDB ID IS EXIST");
            } else if (movieService.IsExistMovieTitle(movie.getTitle().trim())) {
                model.addAttribute("errorMessage", "TITLE IS EXIST");
            } else if (movie.getBackdrops() == null) {
                model.addAttribute("errorMessage", "BACKDROPS IS EMPTY");
            } else if (movie.getGenre_ids() == null) {
                model.addAttribute("errorMessage", "GENRES IS EMPTY");
            } else {
                movie.setActive(true);
                movieService.saveMovie(movie);
                return "redirect:/be/movies/listMovies?successMessage=CREATE%20MOVIE%20SUCCESS";
            }

            List<Genre> genreList = genreService.showGenreList();
            model.addAttribute("allGenres", genreList);
            return "movie-form";
        }
    }


    @GetMapping("/edit/{imdbId}")
    public String editMovie(@PathVariable String imdbId, Model model) {
        List<Genre> genreList = genreService.showGenreList();
        model.addAttribute("allGenres", genreList);
        Optional<Movie> movie = movieService.findAMovie(imdbId);
        if (movie.isPresent()) {
            Movie movieFind = movie.get();
            model.addAttribute("movie", movieFind);

        }
        return "movie-form";
    }

    @GetMapping("/add")
    public String addMovie(Model model) {
        List<Genre> genreList = genreService.showGenreList();
        model.addAttribute("allGenres", genreList);
        Movie movie = Movie.builder()
                .id(new ObjectId())
                .active(false)
                .build();
        model.addAttribute("movie", movie);
        return "movie-form";
    }

    @GetMapping("/delete/{imdbId}")
    public String deleteMovie(@PathVariable String imdbId) {

        Optional<Movie> movie = movieService.findAMovie(imdbId);
        if (movie.isPresent()) {
            Movie movieFind = movie.get();
            movieService.deleteMovie(movieFind);
        }
        return "redirect:/be/movies/listMovies?successMessage=DELETE%20MOVIE%20SUCCESS";
    }


}
