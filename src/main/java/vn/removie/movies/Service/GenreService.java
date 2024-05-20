package vn.removie.movies.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.removie.movies.Entity.Genre;
import vn.removie.movies.Repository.GenreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    @Autowired
    public GenreRepository genreRepository;

    public List<Genre> showGenreList(){
        return genreRepository.findAll();
    }
}
