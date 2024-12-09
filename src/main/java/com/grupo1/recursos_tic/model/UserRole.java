package com.grupo1.recursos_tic.model;

import lombok.RequiredArgsConstructor;

/**
 * Roles de usuario
 */
@RequiredArgsConstructor
public enum UserRole {

    AUTHOR("Autor"),
    READER("Lector"),
    ADMIN("Administrador");

    public final String userRole;

    public boolean isAdmin() {
        return this == ADMIN;
    }

    @Override
    public String toString() { return userRole; }

}
