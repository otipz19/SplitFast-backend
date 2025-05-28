package ua.edu.ukma.cyber.soul.splitfast.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.api.AuthenticationControllerApi;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.LoginRequestDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.LoginResponseDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ResetTokenRequestDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ResetTokenResponseDto;
import ua.edu.ukma.cyber.soul.splitfast.security.AuthenticationService;

@RestController
@RequiredArgsConstructor
public class AuthenticationController implements AuthenticationControllerApi {

    private final AuthenticationService service;

    @Override
    public ResponseEntity<LoginResponseDto> loginUser(LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(service.login(loginRequestDto));
    }

    @Override
    public ResponseEntity<ResetTokenResponseDto> resetToken(ResetTokenRequestDto resetTokenRequestDto) {
        return ResponseEntity.ok(service.resetToken(resetTokenRequestDto));
    }
}
