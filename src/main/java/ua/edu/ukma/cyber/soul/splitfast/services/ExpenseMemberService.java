package ua.edu.ukma.cyber.soul.splitfast.services;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ExpenseMemberCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ExpenseMemberListDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.CreateExpenseMemberDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UpdateExpenseMemberDto;
import ua.edu.ukma.cyber.soul.splitfast.criteria.ExpenseMemberCriteria;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ExpenseEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ExpenseMemberEntity;
import ua.edu.ukma.cyber.soul.splitfast.events.DeleteEntityEvent;
import ua.edu.ukma.cyber.soul.splitfast.mappers.ExpenseMemberMapper;
import ua.edu.ukma.cyber.soul.splitfast.mappers.EnumsMapper;
import ua.edu.ukma.cyber.soul.splitfast.mergers.IMerger;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ExpenseMemberRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.CriteriaRepository;
import ua.edu.ukma.cyber.soul.splitfast.validators.IValidator;

import java.util.List;

@Service
public class ExpenseMemberService extends BaseCRUDService<ExpenseMemberEntity, CreateExpenseMemberDto, UpdateExpenseMemberDto, Integer> {

    private final ExpenseService expenseService;
    private final ExpenseMemberMapper mapper;
    private final EnumsMapper enumsMapper;
    
    public ExpenseMemberService(ExpenseMemberRepository repository, CriteriaRepository criteriaRepository,
                                IValidator<ExpenseMemberEntity> validator, IMerger<ExpenseMemberEntity, CreateExpenseMemberDto, UpdateExpenseMemberDto> merger,
                                ApplicationEventPublisher eventPublisher, ExpenseService expenseService,
                                ExpenseMemberMapper mapper, EnumsMapper enumsMapper) {
        super(repository, criteriaRepository, validator, merger, eventPublisher, ExpenseMemberEntity.class, ExpenseMemberEntity::new);
        this.expenseService = expenseService;
        this.mapper = mapper;
        this.enumsMapper = enumsMapper;
    }

    @Transactional(readOnly = true)
    public ExpenseMemberListDto getListResponseByCriteria(int expenseId, ExpenseMemberCriteriaDto criteriaDto) {
        expenseService.getById(expenseId); // validate expense exists and current user has access

        ExpenseMemberCriteria criteria = new ExpenseMemberCriteria(criteriaDto, expenseId, enumsMapper);
        List<ExpenseMemberEntity> members = criteriaRepository.find(criteria);
        long total = count(criteria);
        return mapper.toListResponse(total, members);
    }

    @Transactional
    public int createExpenseMember(int expenseId, CreateExpenseMemberDto createDto) {
        ExpenseMemberEntity entity = entitySupplier.get();
        merger.mergeForCreate(entity, createDto);
        entity.setExpense(expenseService.getByIdWithoutValidation(expenseId));
        validator.validForCreate(entity);
        return repository.save(entity).getId();
    }
    
    @EventListener
    public void clearExpenseMembers(DeleteEntityEvent<ExpenseEntity, Integer> deleteEvent) {
        ((ExpenseMemberRepository) repository).deleteAllByExpenseId(deleteEvent.getId());
    }
}
