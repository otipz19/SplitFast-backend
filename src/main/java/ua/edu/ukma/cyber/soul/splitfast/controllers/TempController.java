package ua.edu.ukma.cyber.soul.splitfast.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TempController {

    @GetMapping("/temp")
    public String temp() {
        return "Temp";
    }
}
