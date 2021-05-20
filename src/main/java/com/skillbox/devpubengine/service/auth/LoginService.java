package com.skillbox.devpubengine.service.auth;

import com.skillbox.devpubengine.api.request.auth.LoginRequest;
import com.skillbox.devpubengine.api.response.auth.LoginResponse;
import com.skillbox.devpubengine.api.response.general.GenericResultResponse;
import com.skillbox.devpubengine.model.UserEntity;
import com.skillbox.devpubengine.repository.UserRepository;
import com.skillbox.devpubengine.utils.mapper.UserMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public LoginService(AuthenticationManager authenticationManager, UserRepository userRepository, UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Transactional
    public LoginResponse authorize(LoginRequest request) {
        Authentication auth = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        User user = (User) auth.getPrincipal();
        return getResponse(user.getUsername());
    }

    @Transactional
    public LoginResponse getResponse(String login) {
        UserEntity currentUser = userRepository
                .findByEmail(login)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Cannot find login %s", login)));
        currentUser.setCode(null);
        userRepository.save(currentUser);
        return new LoginResponse(true, userMapper.userEntityToLoginUserData(currentUser));
    }

    @Transactional
    public GenericResultResponse logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null)
        {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return new GenericResultResponse(true);
    }
}
