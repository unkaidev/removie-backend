package vn.removie.movies.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.removie.movies.Entity.Movie;
import vn.removie.movies.Repository.MovieRepository;

import java.util.*;

@Service
@EnableScheduling
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    public Optional<Movie> findAMovie(String imdbId) {
        return movieRepository.findMovieByImdbId(imdbId);
    }

    public Optional<Movie> singleMovie(String imdbId) {
        Optional<Movie> movie = movieRepository.findMovieByImdbId(imdbId);
        movie.ifPresent(this::increaseView);
        return movie;
    }


    public void increaseView(Movie movie) {
        movie.setViewToday(movie.getViewToday() + 1);
        movie.setViewThisWeek(movie.getViewThisWeek() + 1);
        movie.setPopularity(movie.getPopularity() + 1);
        movieRepository.save(movie);
    }


    public List<Movie> allMovies() {
        return movieRepository.findAll(Sort.by(Sort.Direction.DESC, "createAt"));
    }

    public List<Movie> allMoviesByReleaseDateDesc() {
        return movieRepository.findAll(Sort.by(Sort.Direction.DESC, "releaseDate"));
    }

    public List<Movie> allMoviesByPopularity() {
        return movieRepository.findAll(Sort.by(Sort.Direction.DESC, "popularity"));
    }

    public List<Movie> allMoviesByTitleAsc() {
        return movieRepository.findAll(Sort.by(Sort.Direction.ASC, "title"));
    }

    public List<Movie> allMoviesByTitleDesc() {
        return movieRepository.findAll(Sort.by(Sort.Direction.DESC, "title"));
    }


    public Page<Movie> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Movie> list;
        if (allMovies().size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allMovies().size());
            list = allMovies().subList(startItem, toIndex);
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createAt");

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);

        Page<Movie> moviePage = new PageImpl<Movie>(list, pageRequest, allMovies().size());
        return moviePage;
    }


    public Page<Movie> findPaginatedByTitleDesc(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Movie> list;
        if (allMoviesByTitleDesc().size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allMoviesByTitleDesc().size());
            list = allMoviesByTitleDesc().subList(startItem, toIndex);
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "title");

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);

        Page<Movie> moviePage = new PageImpl<Movie>(list, pageRequest, allMoviesByTitleDesc().size());

        return moviePage;
    }

    public Page<Movie> findPaginatedByTitleAsc(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Movie> list;
        if (allMoviesByTitleAsc().size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allMoviesByTitleAsc().size());
            list = allMoviesByTitleAsc().subList(startItem, toIndex);
        }

        Sort sort = Sort.by(Sort.Direction.ASC, "title");

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);

        Page<Movie> moviePage = new PageImpl<Movie>(list, pageRequest, allMoviesByTitleAsc().size());

        return moviePage;
    }

    public Page<Movie> findPaginatedByReleaseDateDesc(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Movie> list;
        if (allMoviesByReleaseDateDesc().size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allMoviesByReleaseDateDesc().size());
            list = allMoviesByReleaseDateDesc().subList(startItem, toIndex);
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "releaseDate");

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);

        Page<Movie> moviePage = new PageImpl<Movie>(list, pageRequest, allMoviesByReleaseDateDesc().size());

        return moviePage;
    }

    public Page<Movie> findPaginatedByPopularity(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Movie> list;
        if (allMoviesByPopularity().size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allMoviesByPopularity().size());
            list = allMoviesByPopularity().subList(startItem, toIndex);
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "popularity");

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);

        Page<Movie> moviePage = new PageImpl<Movie>(list, pageRequest, allMoviesByPopularity().size());

        return moviePage;
    }


    public void saveMovie(Movie movie) {
        Date timeStamp = Calendar.getInstance().getTime();
        movie.setCreateAt(timeStamp);
        movieRepository.save(movie);
    }


    public void deleteMovie(Movie movie) {
        movieRepository.delete(movie);
    }

    public boolean IsExistMovieImdbId(String imdbId) {
        boolean check = false;
        for (Movie movie : allMovies()
        ) {
            if (movie.getImdbId().equals(imdbId)) {
                check = true;
            }
        }
        return check;
    }

    public boolean IsExistMovieTitle(String title) {
        boolean check = false;
        for (Movie movie : allMovies()
        ) {
            if (movie.getTitle().equals(title)) {
                check = true;
            }
        }
        return check;
    }

    public Page<Movie> findPaginatedByTitle(String title, Pageable pageable) {
        return movieRepository.findByTitleContainingIgnoreCase(title, pageable);
    }


    public Page<Movie> findTrendingMovies(String dateInput, Pageable pageable) {
        List<Movie> trendingMovies = Collections.emptyList();
        Sort sort = null;

        if (dateInput.equals("day")) {
            sort = Sort.by(Sort.Direction.DESC, "viewToday");
        } else if (dateInput.equals("week")) {
            sort = Sort.by(Sort.Direction.DESC, "viewThisWeek");
        } else {
            return new PageImpl<>(trendingMovies, pageable, 0);
        }

        // Sử dụng phương thức mới để tìm kiếm phim nổi bật với sắp xếp tùy chỉnh
        trendingMovies = movieRepository.findAll(sort);

        return paginateTrendingMovies(trendingMovies, pageable);
    }

    private Page<Movie> paginateTrendingMovies(List<Movie> trendingMovies, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<Movie> list;
        if (trendingMovies.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, trendingMovies.size());
            list = trendingMovies.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, pageable, trendingMovies.size());
    }


    @Scheduled(cron = "0 0 0 * * *")
    public void resetDailyViewCount() {
        List<Movie> movies = movieRepository.findAll();
        for (Movie movie : movies) {
            movie.setViewToday(0);
            movieRepository.save(movie);
        }
    }

    @Scheduled(cron = "0 0 0 * * MON")
    public void resetWeeklyViewCount() {
        List<Movie> movies = movieRepository.findAll();
        for (Movie movie : movies) {
            movie.setViewThisWeek(0);
            movieRepository.save(movie);
        }
    }

    public float getRating(String imdbId) {
        Optional<Movie> movie = movieRepository.findMovieByImdbId(imdbId);
        return movie.map(Movie::getVote_average).orElse(0F);
    }

    public void updateRating(String imdbId, float newRating) {
        Movie movie = singleMovie(imdbId).get();
        if (movie != null) {
            float currentRating = movie.getVote_average();
            long currentVoteCount = movie.getVote_count();

            float updatedRating = ((currentRating * currentVoteCount) + newRating * 2) / (currentVoteCount + 1);
            movie.setVote_average(updatedRating);
            movie.setVote_count(currentVoteCount + 1);

            movieRepository.save(movie);
        }
    }

}
