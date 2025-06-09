package ua.edu.ukma.cyber.soul.splitfast.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.api.DebtRepaymentRequestControllerApi;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.CreateDebtRepaymentRequestDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtRepaymentRequestCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtRepaymentRequestListDto;
import ua.edu.ukma.cyber.soul.splitfast.services.DebtRepaymentRequestService;

@RestController
@RequiredArgsConstructor
public class DebtRepaymentRequestController implements DebtRepaymentRequestControllerApi {

    private final DebtRepaymentRequestService service;

    @Override
    public ResponseEntity<DebtRepaymentRequestListDto> getDebtRepaymentRequestsByCriteria(DebtRepaymentRequestCriteriaDto criteriaDto) {
        return ResponseEntity.ok(service.getListResponseByCriteria(criteriaDto));
    }

    @Override
    public ResponseEntity<Integer> createDebtRepaymentRequest(CreateDebtRepaymentRequestDto createDebtRepaymentRequestDto) {
        return ResponseEntity.ok(service.create(createDebtRepaymentRequestDto));
    }

    @Override
    public ResponseEntity<Void> acceptDebtRepaymentRequest(Integer id) {
        service.acceptDebtRepaymentRequest(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> declineDebtRepaymentRequest(Integer id) {
        service.declineDebtRepaymentRequest(id);
        return ResponseEntity.noContent().build();
    }
}