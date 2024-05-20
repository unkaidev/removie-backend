package vn.removie.movies.BE.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import vn.removie.movies.Service.GenreService;

@Controller
@RequiredArgsConstructor
public class GenreBEController {
    @Autowired
    public GenreService genreService;

}
