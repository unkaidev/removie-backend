package vn.removie.movies.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.removie.movies.Entity.Movie;
import vn.removie.movies.Entity.User;
import vn.removie.movies.Entity.Wishlist;
import vn.removie.movies.Payload.Response.MessageResponse;
import vn.removie.movies.Service.MovieService;
import vn.removie.movies.Service.UserService;
import vn.removie.movies.Service.WishListService;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/wishlist")
@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")

public class WishlistController {
    @Autowired
    private WishListService wishListService;

    @Autowired
    private UserService userService;

    @Autowired
    private MovieService movieService;

    @GetMapping("/list")
    public ResponseEntity<MessageResponse> getMovieFromWishList(@RequestParam String username) {
        Optional<User> userOptional = userService.anUser(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Wishlist wishlist = user.getWishlistId();
            if (wishlist != null && !wishlist.getMovies().isEmpty()) {
                Set<Movie> movies = wishlist.getMovies();
                return ResponseEntity.ok().body(new MessageResponse(0, "Movies found", movies));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse(-1, "Wishlist is empty or not found", null));
            }
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse(-1, "User not found", null));
        }
    }

    @PostMapping("/{imdbId}/add")
    public ResponseEntity<MessageResponse> addMovieToWishList(@PathVariable String imdbId, @RequestParam String username) {
        Optional<User> userOptional = userService.anUser(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Wishlist wishlist = user.getWishlistId();
            if (wishlist == null) {
                wishlist = new Wishlist();
                wishListService.saveWishList(wishlist);
                user.setWishlistId(wishlist);
                userService.saveUser(user);
            }

            Optional<Movie> movieOptional = movieService.findAMovie(imdbId);
            if (movieOptional.isPresent()) {
                Movie movie = movieOptional.get();
                Set<Movie> moviesInWishlist = wishlist.getMovies();


                if (!moviesInWishlist.contains(movie)) {
                    moviesInWishlist.add(movie);
                    wishlist.setMovies(moviesInWishlist);
                    wishListService.saveWishList(wishlist);
                    return ResponseEntity.ok().body(new MessageResponse(0, "Add movie to wish list successfully", null));
                } else {
                    return ResponseEntity.badRequest().body(new MessageResponse(-1, "Movie already exists in the wishlist", null));
                }
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse(-1, "Movie not found", null));
            }
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse(-1, "User not found", null));
        }
    }
    @PostMapping("/{imdbId}/remove")
    public ResponseEntity<MessageResponse> removeMovieToWishList(@PathVariable String imdbId, @RequestParam String username) {
        Optional<User> userOptional = userService.anUser(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Wishlist wishlist = user.getWishlistId();
            if (wishlist == null) {
                return ResponseEntity.badRequest().body(new MessageResponse(-1, "Wishlist not found", null));
            }

            Optional<Movie> movieOptional = movieService.findAMovie(imdbId);
            if (movieOptional.isPresent()) {
                Movie movie = movieOptional.get();
                Set<Movie> moviesInWishlist = wishlist.getMovies();

                if (moviesInWishlist.contains(movie)) {
                    moviesInWishlist.remove(movie);
                    wishlist.setMovies(moviesInWishlist);
                    wishListService.saveWishList(wishlist);
                    return ResponseEntity.ok().body(new MessageResponse(0, "Remove movie from wish list successfully", null));
                } else {
                    return ResponseEntity.badRequest().body(new MessageResponse(-1, "Movie does not exist in the wishlist", null));
                }
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse(-1, "Movie not found", null));
            }
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse(-1, "User not found", null));
        }
    }


}
