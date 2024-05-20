package vn.removie.movies.BE.Controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.removie.movies.Entity.Cast;
import vn.removie.movies.Entity.Credit;
import vn.removie.movies.Entity.Crew;
import vn.removie.movies.Entity.Movie;
import vn.removie.movies.Repository.CastRepository;
import vn.removie.movies.Repository.CrewRepository;
import vn.removie.movies.Repository.MovieRepository;
import vn.removie.movies.Service.CreditService;
import vn.removie.movies.Service.MovieService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/be/credits")
@PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
public class CreditBEController {

    @Autowired
    private CreditService creditService;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private CrewRepository crewRepository;
    @Autowired
    private CastRepository castRepository;

    private void addPaginatedAttributesToModel(Model model, Page<Credit> creditPage, int currentPage) {
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("creditPage", creditPage);
        model.addAttribute("totalPages", creditPage.getTotalPages());

        List<Integer> pageNumbers = IntStream.rangeClosed(1, creditPage.getTotalPages())
                .boxed()
                .collect(Collectors.toList());
        model.addAttribute("pageNumbers", pageNumbers);
    }
    @GetMapping("/listCredits")
    public String showCreditList(Model model,
                                @RequestParam("page") Optional<Integer> page,
                                @RequestParam("size") Optional<Integer> size,
                                @RequestParam(required = false) String successMessage) {

        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
        }

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(4);

        Page<Credit> creditPage = creditService.findPaginated(
                PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.DESC, "createAt")));

        addPaginatedAttributesToModel(model, creditPage, currentPage);

        return "credit-list";
    }

    @GetMapping("/listCreditsByTitleAsc")
    public String showCreditListByTitleAsc(Model model,
                                          @RequestParam("page") Optional<Integer> page,
                                          @RequestParam("size") Optional<Integer> size,
                                          @RequestParam(required = false) String successMessage) {

        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
        }

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(4);

        Page<Credit> creditPage = creditService.findPaginatedByTitleAsc(
                PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.ASC, "title")));

        addPaginatedAttributesToModel(model, creditPage, currentPage);

        return "credit-list";
    }

    @GetMapping("/listCreditsByTitleDesc")
    public String showCreditListByTitleDesc(Model model,
                                           @RequestParam("page") Optional<Integer> page,
                                           @RequestParam("size") Optional<Integer> size,
                                           @RequestParam(required = false) String successMessage) {

        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
        }

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(4);

        Page<Credit> creditPage = creditService.findPaginatedByTitleDesc(
                PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.DESC, "title")));

        addPaginatedAttributesToModel(model, creditPage, currentPage);

        return "credit-list";
    }

    @GetMapping("/search-by-title")
    public String accountList(@RequestParam(name = "search", required = false) String search,
                              @RequestParam("page") Optional<Integer> page,
                              @RequestParam("size") Optional<Integer> size,
                              Model model) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(4);

        if (search != null && !search.isEmpty()) {
            Page<Credit> creditPage = creditService.findPaginatedByTitle(
                    search, PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.DESC, "createAt")));

            addPaginatedAttributesToModel(model, creditPage, currentPage);

        } else {
            return "redirect:/be/credits/listCredits";
        }
        return "credit-list";
    }

    private void populateModel(Model model) {
        List<Movie> movieList = movieRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allMovies", movieList);
        List<Cast> castList = castRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allCasts", castList);
        List<Crew> crewList = crewRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        model.addAttribute("allCrews", crewList);
    }
    @PostMapping("/save")
    public String saveCredit(@ModelAttribute Credit credit, Model model) {
        if (credit.isActive()) {
            credit.setTitle(credit.getTitle().trim());
            creditService.save(credit);
            return "redirect:/be/credits/listCredits?successMessage=EDIT%20CREDIT%20SUCCESS";
        } else {
            if (creditService.IsExistCreditTitle(credit.getTitle().trim())) {
                model.addAttribute("errorMessage", "TITLE IS EXIST");
            } else {
                credit.setActive(true);
                creditService.save(credit);
                return "redirect:/be/credits/listCredits?successMessage=CREATE%20CREDIT%20SUCCESS";
            }
        }
        populateModel(model);
        return "credit-form";
    }

    @GetMapping("/edit/{id}")
    public String editCredit(@PathVariable String id, Model model) {
        populateModel(model);

        Optional<Credit> credit = creditService.findACredit(id);
        if (credit.isPresent()) {
            Credit creditFind = credit.get();
            model.addAttribute("credit", creditFind);
        }
        return "credit-form";
    }


    @GetMapping("/add")
    public String addCredit(Model model) {
        populateModel(model);
        Credit credit = Credit.builder()
                .id(new ObjectId())
                .active(false)
                .build();
        model.addAttribute("credit", credit);
        return "credit-form";
    }

    @GetMapping("/delete/{imdbId}")
    public String deleteCredit(@PathVariable String imdbId) {

        Optional<Credit> credit = creditService.findACredit(imdbId);
        if (credit.isPresent()) {
            Credit creditFind = credit.get();
            creditService.delete(creditFind);
        }
        return "redirect:/be/credits/listCredits?successMessage=DELETE%20CREDIT%20SUCCESS";
    }
}
