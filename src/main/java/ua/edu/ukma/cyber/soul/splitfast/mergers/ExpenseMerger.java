package ua.edu.ukma.cyber.soul.splitfast.mergers;

import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UpdateExpenseDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ExpenseEntity;

@Component
public class ExpenseMerger implements IMerger<ExpenseEntity, UpdateExpenseDto> {

    @Override
    public void mergeForCreate(ExpenseEntity entity, UpdateExpenseDto view) {
        merge(entity, view);
    }

    @Override
    public void mergeForUpdate(ExpenseEntity entity, UpdateExpenseDto view) {
        merge(entity, view);
    }

    private void merge(ExpenseEntity entity, UpdateExpenseDto view) {
        entity.setName(view.getName());
        entity.setDescription(view.getDescription());
    }
}
