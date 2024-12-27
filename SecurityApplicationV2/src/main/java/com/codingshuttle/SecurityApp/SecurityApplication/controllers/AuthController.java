package com.codingshuttle.SecurityApp.SecurityApplication.controllers;


import com.codingshuttle.SecurityApp.SecurityApplication.dto.LoginDto;
import com.codingshuttle.SecurityApp.SecurityApplication.dto.LoginResponseDto;
import com.codingshuttle.SecurityApp.SecurityApplication.dto.SignupDto;
import com.codingshuttle.SecurityApp.SecurityApplication.dto.UserDto;
import com.codingshuttle.SecurityApp.SecurityApplication.entities.User;
import com.codingshuttle.SecurityApp.SecurityApplication.services.AuthService;
import com.codingshuttle.SecurityApp.SecurityApplication.services.SessionService;
import com.codingshuttle.SecurityApp.SecurityApplication.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

   private final UserService userService;
   private final AuthService authService;
   private final SessionService sessionService;


    @Value("${deploy.env}")
    private String deployEnv;



    @PostMapping(value = "/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupDto signupDto){
     UserDto userDto=userService.signup(signupDto);
     return ResponseEntity.ok(userDto);
    }

    //storing cookies with the help of HttpServletResponse
 /*   @PostMapping("/login")
 public ResponseEntity<String> login(@RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response){

     String token=authService.login(loginDto);
     Cookie cookie=new Cookie("token",token);
     cookie.setHttpOnly(true);
     response.addCookie(cookie);
     return ResponseEntity.ok(token);
    }
}
*/

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response){

        LoginResponseDto loginResponseDto=authService.login(loginDto);

        Cookie cookie=new Cookie("refreshToken",loginResponseDto.getRefreshToken());
        cookie.setHttpOnly(true);
        //cookie.setSecure(true);
        cookie.setSecure("production".equals(deployEnv));
        response.addCookie(cookie);
       return ResponseEntity.ok(loginResponseDto);


    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(HttpServletRequest request){

        //Cookie[] cookies= request.getCookies();

       String refreshToken= Arrays.stream(request.getCookies())
                .filter(cookie->"refreshToken".equals(cookie.getName()))
                .findFirst()
               .map(Cookie::getValue)
                .orElseThrow(()-> new AuthenticationServiceException("refresh token not " +
                        "found inside the cookies"));

       //call authservice by passing above token
        LoginResponseDto loginResponseDto=  authService.refreshToken(refreshToken);
        return ResponseEntity.ok(loginResponseDto);
    }

    //logout
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request,HttpServletResponse response){

        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        sessionService.removeSession(user);


        Cookie cookie = new Cookie("refreshToken", null);

        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setSecure("production".equals(deployEnv));
        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out");

    }


  /*  @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@CookieValue("token") String token){

        if(sessionService.isValidToken(token))
            return ResponseEntity.ok("Valid Token");
        else
            return  ResponseEntity.status(401).body("Invalid Token");
    }*/


    /*@PostMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue("token") String token,HttpServletResponse response){

        Long userId=authService.getUserIdByToken(token);

        sessionService.removeSession(userId);

        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return ResponseEntity.ok("User logged out ");

    }*/

}