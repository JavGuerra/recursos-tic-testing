package com.grupo1.recursos_tic.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
public class LoginController {

    /**
     * Muestra la página de login.
     * @return Plantilla login.
     */
    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Muestra la páginan de logout.
     * @return Plantilla logout.
     */
    @RequestMapping("/logout")
    public String logout() {
        return "logout";
    }
}
