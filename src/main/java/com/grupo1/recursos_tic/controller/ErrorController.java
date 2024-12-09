package com.grupo1.recursos_tic.controller;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ErrorController {

//    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
//    @ExceptionHandler(Exception.class)
//    public String handleException(Exception ex, Model model) {
//        model.addAttribute("message", ex.getMessage());
//        return "error";
//    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(
            IllegalArgumentException ex, Model model,
            HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleResponseStatus(
            NoSuchElementException ex, Model model,
            HttpServletResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("message", ex.getMessage());
        return "error";
    }


    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(
            RuntimeException ex, Model model,
            HttpServletResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

    // Nuevos métodos por cada error aquí, si se desea.

    // Capturar el estátus que se devuelve en la excepción
    @ExceptionHandler(ResponseStatusException.class)
    public String handleResponseStatus(
            ResponseStatusException ex, Model model,
            HttpServletResponse response) {
        response.setStatus(ex.getStatusCode().value());
        model.addAttribute("message", ex.getMessage());
        return "error";
    }

}
