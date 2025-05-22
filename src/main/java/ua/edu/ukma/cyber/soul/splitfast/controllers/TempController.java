package ua.edu.ukma.cyber.soul.splitfast.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.api.TempControllerApi;

@RestController
public class TempController implements TempControllerApi {

    @Override
    public ResponseEntity<String> temp() {
        return ResponseEntity.ok("temp");
    }
}
