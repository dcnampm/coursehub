package dev.nampd.coursehub.auth;

import dev.nampd.coursehub.model.entity.Student;
import dev.nampd.coursehub.repository.StudentRepository;
import dev.nampd.coursehub.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final StudentRepository studentRepository;

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );

        var foundUser = studentRepository.findByEmail(authenticationRequest.getUsername());
        if (foundUser.isPresent()) {
            Student user = foundUser.get();
            var accessToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);

            return new AuthenticationResponse(accessToken, refreshToken);
        } else {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);

        if (username != null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                var accessToken = jwtService.generateToken(userDetails);

                Date refreshTokenExpiryDate = jwtService.extractExpiration(refreshToken);
                long remainingTime = refreshTokenExpiryDate.getTime() - System.currentTimeMillis();

                String newRefreshToken;
                if (remainingTime < jwtService.getJwtExpiration()) {
                    newRefreshToken = jwtService.generateRefreshToken(userDetails);
                } else {
                    newRefreshToken = refreshToken;
                }

                return new AuthenticationResponse(accessToken, newRefreshToken);
            }
        }
        return null;
    }
}
