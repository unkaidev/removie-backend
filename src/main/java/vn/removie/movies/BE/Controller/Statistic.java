package vn.removie.movies.BE.Controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.removie.movies.Entity.Movie;
import vn.removie.movies.Entity.User;
import vn.removie.movies.Repository.MovieRepository;
import vn.removie.movies.Repository.UserRepository;

@Controller
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/be/statistic")
@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
public class Statistic {
    @Autowired
    UserRepository userRepository;
    @Autowired
    MovieRepository movieRepository;

    @GetMapping
    public String showStat(Model model) {
        long totalUsers = userRepository.findAll().size();
        long totalMovies = movieRepository.findAll().size();
        User newestUser = userRepository.findFirstByOrderByIdDesc();
        Movie newestMovie = movieRepository.findFirstByOrderByReleaseDateDesc();

        model.addAttribute("totalUsers",totalUsers);
        model.addAttribute("totalMovies",totalMovies);
        model.addAttribute("newestUser",newestUser);
        model.addAttribute("newestMovie",newestMovie);

        return "statistic";
    }
}
