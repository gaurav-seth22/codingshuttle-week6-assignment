package com.codingshuttle.SecurityApp.SecurityApplication.services;


import com.codingshuttle.SecurityApp.SecurityApplication.dto.LoginDto;
import com.codingshuttle.SecurityApp.SecurityApplication.dto.LoginResponseDto;
import com.codingshuttle.SecurityApp.SecurityApplication.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;
    private final SessionService sessionService;

    @Transactional
    public LoginResponseDto login(LoginDto loginDto) {
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
        );

        User user= (User) authentication.getPrincipal();

        //invalidate all existing session of user to perform token rotation
        sessionService.removeSession(user);


        String accessToken=jwtService.generateAccessToken(user);
        String refreshToken= jwtService.generateRefreshToken(user);

        sessionService.generateNewSession(user,refreshToken);
        return new LoginResponseDto(user.getId(),accessToken,refreshToken);
    }

    public Long getUserIdByLogin(LoginDto loginDto){
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
        );
        User user= (User) authentication.getPrincipal();

        return user.getId();

    }

    public Long getUserIdByToken(String token){
        return jwtService.getuserIdFromToken(token);
    }

    @Transactional
    public LoginResponseDto refreshToken(String refreshToken) {

        Long userId=getUserIdByToken(refreshToken);
        sessionService.validateSession(refreshToken);
        User user=userService.getUserById(userId);

        //invalidate existing refresh token and also user session
        sessionService.removeSessionByToken(refreshToken);
        sessionService.removeSession(user);

        String newRefreshToken = jwtService.generateRefreshToken(user);
        String accessToken=jwtService.generateAccessToken(user);
        return new LoginResponseDto(user.getId(),accessToken,newRefreshToken);

    }
}
