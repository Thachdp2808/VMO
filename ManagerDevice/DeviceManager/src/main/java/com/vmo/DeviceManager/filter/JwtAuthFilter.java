package com.vmo.DeviceManager.filter;

import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.repositories.UserRepository;
import com.vmo.DeviceManager.services.JwtService;
import com.vmo.DeviceManager.services.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    private final UserRepository userRepository;

    private final UserDetailService userDetailService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String token;
        String useremail ;
        System.out.println(authHeader);
        if (StringUtils.isEmpty(authHeader) && !org.apache.commons.lang3.StringUtils.startsWith(authHeader,"Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }
        token = authHeader.substring(7);
        useremail = jwtService.extractUsername(token);

        if (!StringUtils.isEmpty(useremail) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailService.userDetailsService().loadUserByUsername(useremail);
            if (jwtService.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                System.out.println(userDetails.getAuthorities());
                System.out.println(userDetails.getUsername());
                User user = userRepository.findByEmail(userDetails.getUsername())
                        .orElseThrow(() -> new RuntimeException("User not found with this email: " + userDetails.getUsername()));
                System.out.print(user.getToken());
                if(user.getToken()==null || user.getToken().isEmpty() ){
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    return;
                }
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                System.out.println(authToken.getAuthorities().toString());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}

