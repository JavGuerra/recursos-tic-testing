
package com.grupo1.recursos_tic.controller;

import com.grupo1.recursos_tic.model.User;
import com.grupo1.recursos_tic.model.UserRole;
import com.grupo1.recursos_tic.service.UserService;

import com.grupo1.recursos_tic.util.ErrMsg;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import static com.grupo1.recursos_tic.util.Utility.*;
import static com.grupo1.recursos_tic.util.Utility.userAuth;

@Controller
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private PasswordEncoder passwordEncoder;

    private final String inUseMsg = "\uD83D\uDEAB\uFE0F  ERROR: Usuario o correo electrónico no disponible.";

    // http://localhost:8082/users
    @GetMapping("users")
    public String findAll(Model model) {
        if (isAuth() && userAuth().get().getRole() != UserRole.ADMIN)
            throw new NoSuchElementException(ErrMsg.NOT_FOUND);

        model.addAttribute("users", userService.findAll());
        return "user/list";
    }

    // http://localhost:8082/users/1
    @GetMapping("users/{id}")
    public String findById(@PathVariable Long id, Model model) {
        Optional<User> userOptional = userService.findById(id);

        User user = userOptional.orElseThrow(
                () -> new NoSuchElementException(ErrMsg.NOT_FOUND)
        );

        model.addAttribute("user", user);
        return "user/detail";
    }

    // TODO Revisar la incorporación de este método
    @GetMapping("users2/{id}")
    public String findById2(@PathVariable Long id, Model model) {
        return userService.findById(id)
                .map(user -> {
                    model.addAttribute("user", user);
                    return "user/detail";
                })
                .orElseGet(() -> {
                    // TODO Lanzar una excepción
                    model.addAttribute("message", "User no encontrado");
                    return "error";
                });
    }

    // http://localhost:8082/users/create
    @GetMapping("users/create")
    public String getFormToCreateNewUser(Model model) {
        try {
            model.addAttribute("user", new User());
        } catch (Exception e) {
            throw new NoSuchElementException(ErrMsg.NOT_FOUND);
        }
        return "user/form";
    }

    // http://localhost:8082/users/update/1
    @GetMapping("users/update/{id}")
    public String getFormToEditUser(Model model, @PathVariable Long id) {
        if (invalidIntPosNumber(id) || id == 0)
            throw new IllegalArgumentException(ErrMsg.INVALID_ID);

        Optional<User> userOptional = userService.findById(id);
        userOptional.ifPresent(user -> model.addAttribute("user", user));
        userOptional.orElseThrow(() -> new NoSuchElementException(ErrMsg.NOT_FOUND));
        return "user/form";
    }

    @PostMapping("users")
    public String save(@ModelAttribute User user, Model model) {

        Long id = user.getId();
        String username = user.getUsername();
        String email = user.getEmail();

        boolean userExist = id != null && userService.existsById(id);
        boolean existsByUsername = userService.existsByUsername(username);
        boolean existsByEmail = userService.existsByEmail(email);

        // Si el usuario es nuevo, pero ya existe el nombre de usuario o el correo...
        if (!userExist && (existsByUsername || existsByEmail)) {
            model.addAttribute("error", inUseMsg);
            model.addAttribute("user", user);
            return "user/form";
        }

        Long idByUsername = (existsByUsername) ? userService.findByUsername(username).get().getId() : id;
        Long idByEmail = (existsByEmail) ? userService.findByEmail(email).get().getId() : id;

        // Si el usuario ya existe, pero se cambió su nombre de usuario o su correo a otra que ya existía...
        if (userExist && ( (existsByUsername && !Objects.equals(idByUsername, id)) ||
                (existsByEmail && !Objects.equals(idByEmail, id)) ) ) {
            model.addAttribute("error", inUseMsg);
            return "user/form";
        }

        String oldPasswd = (userExist) ? userService.findById(id).get().getPassword() : null;
        String newPasswd = user.getPassword();

        // Permite cambiar contraseña
        if (!stringIsEmpty(newPasswd)) user.setPassword(passwordEncoder.encode(newPasswd)); // Cambia contraseña
        else if (!stringIsEmpty(oldPasswd)) user.setPassword(oldPasswd); // Mantiene contraseña actual
        else user.setPassword("$2a$10$ZWaFcX0cp4LcSdMEACn7qewVlN2mqK.RrkqUbdBwQuXCd9ixDEfGq"); // por defecto: User1234

        if (user.getRole() == null) user.setRole(UserRole.AUTHOR);

        userService.save(user);

        return "redirect:/users/" + user.getId();
    }

    // TODO Revisar la incorporación de este método
    @PostMapping("users2")
    public String saveUser(@ModelAttribute User user) {
        boolean exists = false;
        if (user.getId() != null) {
            exists = userService.existsById(user.getId());
            if (!exists)
                new EntityNotFoundException(ErrMsg.NOT_FOUND);
        }
        if (! exists) {
            // Crear un nuevo user
            userService.save(user);
        } else {
            // Actualizar un usuario existente
            userService.findById(user.getId()).ifPresent(userDB -> {
                BeanUtils.copyProperties(user, userDB);
                userService.save(userDB);
            });
        }

        return "redirect:/users/" + user.getId();
    }


    // METODO BORRAR
    // http://localhost:8082/users/delete/1
    @GetMapping("users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        if (invalidIntPosNumber(id) || id == 0)
            throw new IllegalArgumentException(ErrMsg.INVALID_ID);

        if (!userService.existsById(id))
            throw new NoSuchElementException(ErrMsg.NOT_FOUND);

        try {
            userService.deleteUserWithDependencies(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        return "redirect:/users";
    }

    // http://localhost:8082/users/delete/all
    @GetMapping("users/delete/all")
    public String deleteAllUsers() {
        try {
            userService.deleteAllUsers();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT); // 409 status().isConflict()
        }
        if (userService.count() != 0) {
            throw new RuntimeException(ErrMsg.NOT_DELETED);
        }
        return "redirect:/users";
    }

}
