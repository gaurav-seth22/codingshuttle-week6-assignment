package com.codingshuttle.SecurityApp.SecurityApplication.filters;

import com.codingshuttle.SecurityApp.SecurityApplication.entities.User;
import com.codingshuttle.SecurityApp.SecurityApplication.services.JwtService;
import com.codingshuttle.SecurityApp.SecurityApplication.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            //there are multiple types of headers but we are interested in Authorization header only
            final String requestTokenHeader = request.getHeader("Authorization");

            //tokens are not presented as string they have prefix "Bearer gdfgjdglkdgjs"

            if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer")) {
                filterChain.doFilter(request, response);
                return;
            }
//below statement will split on the basis of space second string after Bearer and space will be token
            String token = requestTokenHeader.split("Bearer ")[1];

            Long userId = jwtService.getuserIdFromToken(token);

            //if authenticationtoken or user is not set inside SecurityContextholder
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userService.getUserById(userId);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                //now add this user to springsecuritycontext
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }
            //if you don't write below line, request will not go to another filter
            filterChain.doFilter(request, response);
        }catch(Exception ex){
            handlerExceptionResolver.resolveException(request,response,null,ex);
        }
        //Once you hav response from filterchain, you can also log response here

    }
}
