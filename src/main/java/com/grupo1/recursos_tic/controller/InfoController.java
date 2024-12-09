package com.grupo1.recursos_tic.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class InfoController {

    @GetMapping("/info/help.html")
    public String help() {
        return "info/help";
    }

    @GetMapping("/info/legal.html")
    public String legal() {
        return "info/legal";
    }

    @GetMapping("/info/privacy.html")
    public String privacy() {
        return "info/privacy";
    }

    @GetMapping("/info/project.html")
    public String project() {
        return "info/project";
    }
}
