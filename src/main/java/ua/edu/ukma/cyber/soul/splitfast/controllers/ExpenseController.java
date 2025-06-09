package ua.edu.ukma.cyber.soul.splitfast.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.api.ExpenseControllerApi;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ExpenseCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ExpenseDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ExpenseListDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UpdateExpenseDto;
import ua.edu.ukma.cyber.soul.splitfast.services.ExpenseService;

@RestController
@RequiredArgsConstructor
public class ExpenseController implements ExpenseControllerApi {

    private final ExpenseService service;

    @Override
    public ResponseEntity<ExpenseDto> getExpenseById(Integer expenseId) {
        return ResponseEntity.ok(service.getResponseById(expenseId));
    }

    @Override
    public ResponseEntity<ExpenseListDto> getExpensesByCriteria(Integer activityId, ExpenseCriteriaDto criteria) {
        return ResponseEntity.ok(service.getListResponseByCriteria(activityId, criteria));
    }

    @Override
    public ResponseEntity<Integer> createExpense(Integer activityId, UpdateExpenseDto updateExpenseDto) {
        return ResponseEntity.ok(service.createExpense(activityId, updateExpenseDto));
    }

    @Override
    public ResponseEntity<Void> updateExpense(Integer expenseId, UpdateExpenseDto updateExpenseDto) {
        service.update(expenseId, updateExpenseDto);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteExpense(Integer expenseId) {
        service.delete(expenseId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> finishExpense(Integer expenseId) {
        service.finishExpense(expenseId);
        return ResponseEntity.noContent().build();
    }
}
