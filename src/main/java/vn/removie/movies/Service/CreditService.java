package vn.removie.movies.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import vn.removie.movies.Entity.Credit;
import vn.removie.movies.Entity.Movie;
import vn.removie.movies.Repository.CreditRepository;
import vn.removie.movies.Repository.MovieRepository;

import java.util.*;

@Service
public class CreditService {
    @Autowired
    private CreditRepository creditRepository;
    @Autowired
    private MovieRepository movieRepository;

    public List<Credit> allCredits() {
        return creditRepository.findAll(Sort.by(Sort.Direction.DESC, "createAt"));
    }
    public List<Credit> allCreditsByTitleAsc() {
        return creditRepository.findAll(Sort.by(Sort.Direction.ASC, "title"));
    }

    public List<Credit> allCreditsByTitleDesc() {
        return creditRepository.findAll(Sort.by(Sort.Direction.DESC, "title"));
    }

    public Optional<Credit> singleMovieCredit(String imdbId) {
        Optional<Credit> credit = null;
        Optional<Movie> movie = movieRepository.findMovieByImdbId(imdbId);

        if (movie.isPresent()) {
            Optional<Credit> creditFind = creditRepository.findCreditByMovie(movie);
            if (creditFind.isPresent()) {
                credit = creditFind;
            }
        }
        return credit;
    }
    public Page<Credit> findPaginated(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Credit> list;
        if (allCredits().size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allCredits().size());
            list = allCredits().subList(startItem, toIndex);
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createAt");

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);

        Page<Credit> creditPage = new PageImpl<Credit>(list, pageRequest, allCredits().size());
        return creditPage;
    }
    public Page<Credit> findPaginatedByTitleDesc(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Credit> list;
        if (allCreditsByTitleDesc().size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allCreditsByTitleDesc().size());
            list = allCreditsByTitleDesc().subList(startItem, toIndex);
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "title");

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);

        Page<Credit> moviePage = new PageImpl<Credit>(list, pageRequest, allCreditsByTitleDesc().size());

        return moviePage;
    }

    public Page<Credit> findPaginatedByTitleAsc(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Credit> list;
        if (allCreditsByTitleAsc().size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, allCreditsByTitleAsc().size());
            list = allCreditsByTitleAsc().subList(startItem, toIndex);
        }

        Sort sort = Sort.by(Sort.Direction.ASC, "title");

        PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);

        Page<Credit> moviePage = new PageImpl<Credit>(list, pageRequest, allCreditsByTitleAsc().size());

        return moviePage;
    }
    public Page<Credit> findPaginatedByTitle(String title, Pageable pageable) {
        return creditRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    public Optional<Credit> findACredit(String id) {
        return creditRepository.findById(id);
    }

    public void save(Credit credit) {
        Date timeStamp = Calendar.getInstance().getTime();
        credit.setCreateAt(timeStamp);
        creditRepository.save(credit);
    }
    public boolean IsExistCreditTitle(String title) {
        boolean check = false;
        for (Credit credit : allCredits()
        ) {
            if (credit.getTitle().equals(title)) {
                check = true;
            }
        }
        return check;
    }

    public void delete(Credit creditFind) {
        creditRepository.delete(creditFind);
    }
}
