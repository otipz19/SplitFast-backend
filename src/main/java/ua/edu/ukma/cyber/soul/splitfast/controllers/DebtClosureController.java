package ua.edu.ukma.cyber.soul.splitfast.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.api.DebtClosureControllerApi;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.CreateDebtClosureDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtClosureDto;
import ua.edu.ukma.cyber.soul.splitfast.services.DebtClosureService;

@RestController
@RequiredArgsConstructor
public class DebtClosureController implements DebtClosureControllerApi {

    private final DebtClosureService service;

    @Override
    public ResponseEntity<DebtClosureDto> createDebtClosure(CreateDebtClosureDto createDebtClosureDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createDebtClosure(createDebtClosureDto));
    }
}