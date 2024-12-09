package com.grupo1.recursos_tic.util;

import com.grupo1.recursos_tic.model.User;
import com.grupo1.recursos_tic.service.UserService;
import lombok.experimental.UtilityClass;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@UtilityClass
public class Utility {

    private UserService userService;

    /**
     * Comprueba si una cadena está vacía
     *
     * @param str String
     * @return boolean
     */
    public static boolean stringIsEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Comprueba si un número no es un número entero positivo
     *
     * @param num Long
     * @return boolean
     */
    public static boolean invalidIntPosNumber(Long num) {
        return num == null || num < 0L;
    }

    /**
     * Comprueba si esta abierta alguna sesión de usuario.
     * @return true si está abierta la sesión / false en caso contrario
     */
    public static Boolean isAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (authentication != null && !(authentication instanceof AnonymousAuthenticationToken));
    }

    /**
     * Devuelve el usuario de la sesión
     * @return El opcional de usuario autenticado, alternativamente, el opcional de usuario vacío.
     */
    public static Optional<User> userAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (isAuth()) {
            // Obtener el principal de la autenticación
            Object principal = authentication.getPrincipal();

            // Verificar si es tu clase User personalizada
            if (principal instanceof User) {
                return Optional.of((User) principal);
            }

            // Si es un UserDetails de Spring Security, necesitarás recuperar el usuario de tu repositorio
            if (principal instanceof org.springframework.security.core.userdetails.User) {
                // Suponiendo que tienes un userRepository para buscar por username
                String username = ((org.springframework.security.core.userdetails.User) principal).getUsername();
                return userService.findByUsername(username);
            }
        }

        return Optional.empty();

    }

    /**
     * Comprueba si el usuario autenticado es el mismo que el usuario pasado como parámetro
     * @param user El usuario que se quiere comprobar
     * @return true si son iguales / false en caso contrario
     */
    public static boolean confirmUser(User user) {
        return userAuth().map(u -> u.getId().equals(user.getId())).orElse(false);
    }

}
