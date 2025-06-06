package ua.edu.ukma.cyber.soul.splitfast.services;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ExpenseCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ExpenseDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ExpenseListDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UpdateExpenseDto;
import ua.edu.ukma.cyber.soul.splitfast.criteria.ExpenseCriteria;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ExpenseEntity;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.mappers.ExpenseMapper;
import ua.edu.ukma.cyber.soul.splitfast.mergers.IMerger;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ActivityRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ExpenseRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.CriteriaRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.ExpenseRepository.ExpenseCost;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.utils.TimeUtils;
import ua.edu.ukma.cyber.soul.splitfast.validators.ExpenseValidator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseService extends BaseCRUDService<ExpenseEntity, UpdateExpenseDto, UpdateExpenseDto, Integer> {

    private final ExpenseMapper mapper;
    private final SecurityUtils securityUtils;
    private final ActivityRepository activityRepository;

    public ExpenseService(ExpenseRepository repository, CriteriaRepository criteriaRepository,
                          ExpenseValidator validator, IMerger<ExpenseEntity, UpdateExpenseDto, UpdateExpenseDto> merger,
                          ApplicationEventPublisher eventPublisher, ExpenseMapper mapper,
                          SecurityUtils securityUtils, ActivityRepository activityRepository) {
        super(repository, criteriaRepository, validator, merger, eventPublisher, ExpenseEntity.class, ExpenseEntity::new);
        this.mapper = mapper;
        this.securityUtils = securityUtils;
        this.activityRepository = activityRepository;
    }

    @Transactional
    public int createExpense(int activityId, UpdateExpenseDto view) {
        ExpenseEntity entity = entitySupplier.get();
        merger.mergeForCreate(entity, view);
        entity.setActivity(
                activityRepository.findById(activityId)
                        .orElseThrow(() -> new ValidationException("error.expense.activity.not-exists"))
        );
        entity.setOwner(securityUtils.getCurrentUser());
        entity.setTimeCreated(TimeUtils.getCurrentDateTimeUTC());
        validator.validForCreate(entity);
        return repository.save(entity).getId();
    }

    @Transactional
    public void finishExpense(int expenseId) {
        ExpenseEntity expense = getByIdWithoutValidation(expenseId);
        ((ExpenseValidator) validator).validForFinish(expense);
        expense.setTimeFinished(TimeUtils.getCurrentDateTimeUTC());
        // TODO: add actual contact update logic
        repository.save(expense);
    }

    @Transactional(readOnly = true)
    public ExpenseDto getResponseById(int userId) {
        ExpenseEntity entity = getById(userId);
        return mapper.toResponse(entity, getCostMap(entity));
    }

    @Transactional(readOnly = true)
    public ExpenseListDto getListResponseByCriteria(int activityId, ExpenseCriteriaDto criteriaDto) {
        ExpenseCriteria criteria = new ExpenseCriteria(criteriaDto, activityId);
        List<ExpenseEntity> entities = getList(criteria);
        long total = count(criteria);
        if (entities.isEmpty()) return new ExpenseListDto(total, List.of());
        return mapper.toListResponse(total, entities, getCostMap(entities));
    }

    private Map<Integer, BigDecimal> getCostMap(ExpenseEntity entity) {
        return getCostMap(List.of(entity));
    }

    private Map<Integer, BigDecimal> getCostMap(List<ExpenseEntity> entities) {
        List<Integer> ids = entities.stream().map(ExpenseEntity::getId).toList();
        return ((ExpenseRepository) repository).calculateExpensesCosts(ids).stream()
                .collect(Collectors.toMap(ExpenseCost::getId, ExpenseCost::getCost));
    }
}
