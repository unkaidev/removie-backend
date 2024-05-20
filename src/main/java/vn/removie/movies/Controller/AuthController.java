package vn.removie.movies.Controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.removie.movies.Entity.ERole;
import vn.removie.movies.Entity.Wishlist;
import vn.removie.movies.Repository.RoleRepository;
import vn.removie.movies.Entity.Role;
import vn.removie.movies.Entity.User;
import vn.removie.movies.Payload.Request.LoginRequest;
import vn.removie.movies.Payload.Request.SignupRequest;
import vn.removie.movies.Payload.Response.UserInfoResponse;
import vn.removie.movies.Payload.Response.MessageResponse;
import vn.removie.movies.Repository.UserRepository;
import vn.removie.movies.Repository.WishlistRepository;
import vn.removie.movies.Service.WishListService;
import vn.removie.movies.security.jwt.JwtUtils;
import vn.removie.movies.security.services.UserDetailsImpl;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private WishListService wishListService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        boolean userExists = userRepository.existsByUsername(loginRequest.getUsername());
        try {
            if (!userExists) {
                return ResponseEntity
                        . status(404)
                        .body(new MessageResponse(-1, "Error: USERNAME OR PASSWORD IS INCORRECT!",null));
            } else {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

                ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

                List<String> roles = userDetails.getAuthorities().stream()
                        .map(item -> item.getAuthority())
                        .collect(Collectors.toList());


                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                        .body(new UserInfoResponse(userDetails.getId(),
                                userDetails.getUsername(),
                                userDetails.getEmail(),
                                roles, 0, "Sign in success!"));
            }
        }catch(AuthenticationException e) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse(-1, "Error: Sign in!",null));
            }

        }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1,"Error: Username is already taken!",null));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .ok()
                    .body(new MessageResponse(-1,"Error: Email is already in use!",null));
        }

        User user = User.builder()
                .username(signUpRequest.getUsername())
                .email( signUpRequest.getEmail())
                .password(encoder.encode(signUpRequest.getPassword()))
                .build();

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        Wishlist wishlist = new Wishlist();
        wishListService.saveWishList(wishlist);
        user.setWishlistId(wishlist);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse(0,"User registered successfully!",null));
    }
    @PostMapping("/signout")
    public ResponseEntity<?> signout() {
        ResponseCookie jwtCookie = jwtUtils.getCleanJwtCookie();
        if(jwtCookie != null){
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(new MessageResponse(0,"Sign out successfully!",null));
        }else {
            return ResponseEntity.status(500).body(new MessageResponse(-1,"Error sign out!",null));
        }

    }
}