package ua.edu.ukma.cyber.soul.splitfast.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.api.DebtClosureControllerApi;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.CreateDebtClosureDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtClosureCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtClosureDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtClosureListDto;
import ua.edu.ukma.cyber.soul.splitfast.services.DebtClosureService;

@RestController
@RequiredArgsConstructor
public class DebtClosureController implements DebtClosureControllerApi {

    private final DebtClosureService service;

    @Override
    public ResponseEntity<DebtClosureDto> getDebtClosureById(Integer id) {
        return ResponseEntity.ok(service.getResponseById(id));
    }

    @Override
    public ResponseEntity<DebtClosureListDto> getDebtClosuresByCriteria(DebtClosureCriteriaDto criteria) {
        return ResponseEntity.ok(service.getListResponseByCriteria(criteria));
    }

    @Override
    public ResponseEntity<Integer> createDebtClosure(CreateDebtClosureDto createDebtClosureDto) {
        return ResponseEntity.ok(service.create(createDebtClosureDto));
    }
}