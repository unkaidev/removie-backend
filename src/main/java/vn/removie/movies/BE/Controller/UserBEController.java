package vn.removie.movies.BE.Controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.removie.movies.Entity.ERole;
import vn.removie.movies.Entity.Role;
import vn.removie.movies.Entity.User;
import vn.removie.movies.Entity.Wishlist;
import vn.removie.movies.Service.RoleService;
import vn.removie.movies.Service.UserService;
import vn.removie.movies.Service.WishListService;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static vn.removie.movies.Entity.ERole.ROLE_USER;

@Controller
@AllArgsConstructor
@NoArgsConstructor
@RequestMapping("/be/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserBEController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private WishListService wishListService;

    private void addPaginatedAttributesToModel(Model model, Page<User> userPage, int currentPage) {
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("userPage", userPage);
        model.addAttribute("totalPages", userPage.getTotalPages());

        List<Integer> pageNumbers = IntStream.rangeClosed(1, userPage.getTotalPages())
                .boxed()
                .collect(Collectors.toList());
        model.addAttribute("pageNumbers", pageNumbers);
    }

    @GetMapping("/listUsers")
    public String showUserList(Model model,
                               @RequestParam("page") Optional<Integer> page,
                               @RequestParam("size") Optional<Integer> size,
                               @RequestParam(required = false) String successMessage) {

        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
        }

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(4);

        Page<User> userPage = userService.findPaginated(
                PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.DESC, "createAt")));

        addPaginatedAttributesToModel(model, userPage, currentPage);

        return "user-list";
    }

    @GetMapping("/listUsersByUsernameAsc")
    public String showUserListByTitleAsc(Model model,
                                         @RequestParam("page") Optional<Integer> page,
                                         @RequestParam("size") Optional<Integer> size,
                                         @RequestParam(required = false) String successMessage) {

        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
        }

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(4);

        Page<User> userPage = userService.findPaginatedByUsernameAsc(
                PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.ASC, "username")));

        addPaginatedAttributesToModel(model, userPage, currentPage);

        return "user-list";
    }

    @GetMapping("/listUsersByUsernameDesc")
    public String showUserListByTitleDesc(Model model,
                                          @RequestParam("page") Optional<Integer> page,
                                          @RequestParam("size") Optional<Integer> size,
                                          @RequestParam(required = false) String successMessage) {

        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
        }

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(4);

        Page<User> userPage = userService.findPaginatedByUsernameDesc(
                PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.DESC, "username")));

        addPaginatedAttributesToModel(model, userPage, currentPage);

        return "user-list";
    }

    @GetMapping("/search-by-title")
    public String accountList(@RequestParam(name = "search", required = false) String search,
                              @RequestParam("page") Optional<Integer> page,
                              @RequestParam("size") Optional<Integer> size,
                              Model model) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(4);

        if (search != null && !search.isEmpty()) {
            Page<User> userPage = userService.findPaginatedByUsername(
                    search, PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.DESC, "createAt")));

            addPaginatedAttributesToModel(model, userPage, currentPage);

        } else {
            return "redirect:/be/users/listUsers";
        }
        return "user-list";
    }


    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user, Model model) {
        if (user.isActive()) {
            User existingUser = userService.getUserById(user.getId());
            user.setPassword(existingUser.getPassword());
            user.setUsername(user.getUsername().trim());
            user.setEmail(user.getEmail().trim());
            userService.saveUser(user);
            return "redirect:/be/users/listUsers?successMessage=EDIT%20USER%20SUCCESS";
        } else {
            if (userService.isExistUsername(user.getUsername().trim())) {
                model.addAttribute("errorMessage", "USERNAME IS EXIST");
            } else if (userService.isExistEmail(user.getEmail().trim())) {
                model.addAttribute("errorMessage", "EMAIL IS EXIST");
            } else {
                user.setActive(true);
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                user.setPassword(encoder.encode(user.getPassword()));
                Wishlist newWishlist = new Wishlist(new ObjectId(),null);
                wishListService.saveWishList(newWishlist);
                user.setWishlistId(newWishlist);
                if(user.getRoles().isEmpty()){
                    Role role = roleService.findRoleByName(ROLE_USER);
                    Set<Role> roles = new HashSet<>();
                    roles.add(role);
                    user.setRoles(roles);
                }
                userService.saveUser(user);
                return "redirect:/be/users/listUsers?successMessage=CREATE%20USER%20SUCCESS";
            }
            List<Role> roleList = roleService.showRoleList();
            model.addAttribute("allRoles", roleList);
            return "user-form";
        }
    }



    @GetMapping("/edit/{username}")
    public String editUser(@PathVariable String username, Model model) {
        List<Role> roleList = roleService.showRoleList();
        model.addAttribute("allRoles", roleList);
        Optional<User> user = userService.anUser(username);
        if (user.isPresent()) {
            User userFind = user.get();
            model.addAttribute("user", userFind);

        }
        return "user-form";
    }

    @GetMapping("/add")
    public String addUser(Model model) {
        List<Role> roleList = roleService.showRoleList();
        model.addAttribute("allRoles", roleList);
        User user = User.builder()
                .id(new ObjectId())
                .active(false)
                .build();
        model.addAttribute("user", user);
        return "user-form";
    }

    @GetMapping("/delete/{username}")
    public String deleteUser(@PathVariable String username) {

        Optional<User> user = userService.anUser(username);
        if (user.isPresent()) {
            User userFind = user.get();
            userService.deleteUser(userFind);
        }
        return "redirect:/be/users/listUsers?successMessage=DELETE%20MOVIE%20SUCCESS";
    }


}
