package ua.edu.ukma.cyber.soul.splitfast.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.api.DebtRepaymentRequestControllerApi;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.CreateDebtRepaymentRequestDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtRepaymentRequestDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtRepaymentRequestCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtRepaymentRequestListDto;
import ua.edu.ukma.cyber.soul.splitfast.services.DebtRepaymentRequestService;

@RestController
@RequiredArgsConstructor
public class DebtRepaymentRequestController implements DebtRepaymentRequestControllerApi {

    private final DebtRepaymentRequestService service;

    @Override
    public ResponseEntity<DebtRepaymentRequestDto> createDebtRepaymentRequest(CreateDebtRepaymentRequestDto createDebtRepaymentRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createDebtRepaymentRequest(createDebtRepaymentRequestDto));
    }

    @Override
    public ResponseEntity<DebtRepaymentRequestListDto> getDebtRepaymentRequestsByCriteria(DebtRepaymentRequestCriteriaDto criteriaDto) {
        return ResponseEntity.ok(service.getDebtRepaymentRequestsByCriteria(criteriaDto));
    }

    @Override
    public ResponseEntity<Void> submitDebtRepaymentRequest(Integer id) {
        service.submitDebtRepaymentRequest(id);
        return ResponseEntity.noContent().build();
    }
}