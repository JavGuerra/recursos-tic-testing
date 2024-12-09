package com.grupo1.recursos_tic.service;

import com.grupo1.recursos_tic.model.User;
import com.grupo1.recursos_tic.repository.UserRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementación de servicios de detalle de usuario.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepo userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Log para depuración
        System.out.println("Cargando usuario: " + user.getUsername() + ", Rol: " + user.getRole());

        return user;
    }
}