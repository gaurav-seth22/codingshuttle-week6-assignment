package com.codingshuttle.SecurityApp.SecurityApplication.services;

import com.codingshuttle.SecurityApp.SecurityApplication.dto.LoginDto;
import com.codingshuttle.SecurityApp.SecurityApplication.dto.SignupDto;
import com.codingshuttle.SecurityApp.SecurityApplication.dto.UserDto;
import com.codingshuttle.SecurityApp.SecurityApplication.entities.User;
import com.codingshuttle.SecurityApp.SecurityApplication.exceptions.ResourceNotFoundException;
import com.codingshuttle.SecurityApp.SecurityApplication.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;


    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(()-> new BadCredentialsException("User with email "+username+"not found")
                );
    }


    public User getUserById(Long userId){

        return userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User withid "+userId+"not found")
                );
    }

    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
    }
    public UserDto signup(SignupDto signupDto) {
        Optional<User> user=userRepository.findByEmail(signupDto.getEmail());
        if(user.isPresent()){
            throw new BadCredentialsException("user with email already present"+signupDto.getEmail());
        }
          User toBeCreatedUser=modelMapper.map(signupDto,User.class);
          toBeCreatedUser.setPassword(passwordEncoder.encode(toBeCreatedUser.getPassword()));

          User saveduser=userRepository.save(toBeCreatedUser);

          return modelMapper.map(saveduser, UserDto.class);
    }


    public User save(User newUser) {
        return userRepository.save(newUser);
    }
}
