package ua.edu.ukma.cyber.soul.splitfast.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.api.UserControllerApi;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.RegisterUserDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UserCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UserDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UserListDto;
import ua.edu.ukma.cyber.soul.splitfast.services.UserService;

@RestController
@RequiredArgsConstructor
public class UserController implements UserControllerApi {

    private final UserService service;

    @Override
    public ResponseEntity<UserDto> getUserById(Integer userId) {
        return ResponseEntity.ok(service.getResponseById(userId));
    }

    @Override
    public ResponseEntity<UserListDto> getUsersByCriteria(UserCriteriaDto criteria) {
        return ResponseEntity.ok(service.getListResponseByCriteria(criteria));
    }

    @Override
    public ResponseEntity<UserDto> getCurrentUser() {
        return ResponseEntity.ok(service.getCurrentUser());
    }

    @Override
    public ResponseEntity<Integer> registerUser(RegisterUserDto dto) {
        return ResponseEntity.ok(service.registerUser(dto));
    }
}
