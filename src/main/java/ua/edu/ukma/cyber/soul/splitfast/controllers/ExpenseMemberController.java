package ua.edu.ukma.cyber.soul.splitfast.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.api.ExpenseMemberControllerApi;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.*;
import ua.edu.ukma.cyber.soul.splitfast.services.ExpenseMemberService;

@RestController
@RequiredArgsConstructor
public class ExpenseMemberController implements ExpenseMemberControllerApi {

    private final ExpenseMemberService service;

    @Override
    public ResponseEntity<ExpenseMemberDto> getExpenseMemberById(Integer memberId) {
        return ResponseEntity.ok(service.getResponseById(memberId));
    }

    @Override
    public ResponseEntity<ExpenseMemberListDto> getExpenseMembersByCriteria(Integer expenseId, ExpenseMemberCriteriaDto criteria) {
        return ResponseEntity.ok(service.getListResponseByCriteria(expenseId, criteria));
    }

    @Override
    public ResponseEntity<Integer> createExpenseMember(Integer expenseId, CreateExpenseMemberDto createExpenseMemberDto) {
        return ResponseEntity.ok(service.createExpenseMember(expenseId, createExpenseMemberDto));
    }

    @Override
    public ResponseEntity<Void> updateExpenseMember(Integer memberId, UpdateExpenseMemberDto updateExpenseMemberDto) {
        service.update(memberId, updateExpenseMemberDto);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteExpenseMember(Integer memberId) {
        service.delete(memberId);
        return ResponseEntity.noContent().build();
    }

}
