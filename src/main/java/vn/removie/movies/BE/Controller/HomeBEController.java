package vn.removie.movies.BE.Controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vn.removie.movies.security.jwt.JwtUtils;
import vn.removie.movies.security.services.UserDetailsImpl;

@Controller
@RequestMapping("/be")
public class HomeBEController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;

    @GetMapping
    public String showHomePage() {
        return "home";
    }

    @GetMapping("/login")
    public String showLoginPage(
            Model model,
            @RequestParam(required = false) String errorMessage) {
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
        }
        return "login";
    }

    @PostMapping("/authenticateUser")
    public String authenticateUser(@RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   HttpServletResponse response) {
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username.trim(), password));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
            response.setHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());

            Cookie cookie = new Cookie("removie", jwtCookie.getValue());
            cookie.setMaxAge(24 * 60 * 60);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);

            return "redirect:/be/movies/listMovies?successMessage=LOGIN%20SUCCESS";
        } catch (Exception e) {
            return "redirect:/be/login?errorMessage=USERNAME%20PASSWORD%20INCORRECT";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        try {
            Cookie cookie = new Cookie("removie", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
            return "redirect:/be/login";
        } catch (Exception e) {
            return "redirect:/be";
        }

    }

    @GetMapping("/showPage401")
    public String showPage401() {
        return "errors/401";
    }

    @GetMapping("/showPage403")
    public String showPage403() {
        return "errors/403";
    }
}
