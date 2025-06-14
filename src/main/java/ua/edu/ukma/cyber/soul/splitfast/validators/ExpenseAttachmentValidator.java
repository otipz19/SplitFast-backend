package ua.edu.ukma.cyber.soul.splitfast.validators;

import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ExpenseEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.enums.FileType;
import ua.edu.ukma.cyber.soul.splitfast.repositories.IRepository;

@Component
public class ExpenseAttachmentValidator extends BaseFileValidator<ExpenseEntity> {

    public ExpenseAttachmentValidator(IRepository<ExpenseEntity, Integer> repository, IValidator<ExpenseEntity> validator) {
        super(repository, validator, ".{1,255}");
    }

    @Override
    public FileType supportedFileType() {
        return FileType.EXPENSE_ATTACHMENT;
    }
}
