package ua.edu.ukma.cyber.soul.splitfast.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.api.UserControllerApi;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.RegisterUserDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UserDto;
import ua.edu.ukma.cyber.soul.splitfast.services.UserService;

@RestController
@RequiredArgsConstructor
public class UserController implements UserControllerApi {

    private final UserService service;

    @Override
    public ResponseEntity<UserDto> getCurrentUser() {
        return null;
    }

    @Override
    public ResponseEntity<Integer> registerUser(RegisterUserDto dto) {
        return ResponseEntity.ok(service.registerUser(dto));
    }
}
