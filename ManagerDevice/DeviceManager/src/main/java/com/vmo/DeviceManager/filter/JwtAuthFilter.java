package com.vmo.DeviceManager.filter;

import com.vmo.DeviceManager.exceptions.model.UnauthorizedException;
import com.vmo.DeviceManager.exceptions.model.UserException;
import com.vmo.DeviceManager.models.User;
import com.vmo.DeviceManager.repositories.UserRepository;
import com.vmo.DeviceManager.services.JwtService;
import com.vmo.DeviceManager.services.UserDetailService;
import io.jsonwebtoken.ExpiredJwtException;
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
import org.apache.commons.lang3.StringUtils;

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
        //Kiểm tra nếu rỗng thì bỏ quá đẻ sử dụng cho những API Login...
        if (StringUtils.isEmpty(authHeader) || !org.apache.commons.lang3.StringUtils.startsWith(authHeader,"Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }
        token = authHeader.substring(7);
        //Ném ra lỗi 401 nếu như hết hạn token
        try{
            useremail = jwtService.extractUsername(token);
        }catch (ExpiredJwtException u){
            response.setStatus(401);
            response.getWriter().print("Unauthorized");
            return;
        }

        if (!StringUtils.isEmpty(useremail) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailService.userDetailsService().loadUserByUsername(useremail);
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    User user = userRepository.findByEmail(userDetails.getUsername())
                            .orElseThrow(() -> new UserException(userDetails.getUsername()));
                    //Kiểm tra user xem có token hay chưa
                    if(user.getToken()==null || user.getToken().isEmpty()  ){
                        response.setStatus(401);
                        response.getWriter().print("Unauthorized");
                        return;
                    }
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    //Lưu authentoken vào authentication để có thể lấy ra
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
        }
        filterChain.doFilter(request, response);
    }
}

