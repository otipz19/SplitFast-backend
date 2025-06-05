package ua.edu.ukma.cyber.soul.splitfast.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.CreateDebtClosureDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtClosureDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.DebtClosure;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersAssociation;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.NotFoundException;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.mappers.DebtClosureMapper;
import ua.edu.ukma.cyber.soul.splitfast.repositories.DebtClosureRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.DebtRepaymentRequestRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.UserRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;
import ua.edu.ukma.cyber.soul.splitfast.validators.DebtClosureValidator;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DebtClosureService {

    private final DebtClosureRepository repository;
    private final UserRepository userRepository;
    private final DebtClosureMapper mapper;
    private final DebtClosureValidator validator;
    private final SecurityUtils securityUtils;
    private final DebtService debtService;
    private final DebtRepaymentRequestRepository requestRepository;
    @Transactional
    public DebtClosureDto createDebtClosure(CreateDebtClosureDto createDto) {
        UserEntity executorUser = securityUtils.getCurrentUser();
        UserEntity debtorUser = userRepository.findById(createDto.getDebtorUserId())
                .orElseThrow(() -> new NotFoundException(UserEntity.class, "id: " + createDto.getDebtorUserId()));

        boolean alreadyExists = requestRepository.existsByAssociation_FirstUserIdAndAssociation_SecondUserIdAndSubmittedFalse(
                executorUser.getId(), debtorUser.getId()
        );

        if (alreadyExists) {
            throw new ValidationException("error.debt-request.pending-request");
        }

        BigDecimal debtorToExecutorDebt = debtService.getDebtAmount(debtorUser.getId(), executorUser.getId());

        validator.validateDebtClosureAmount(createDto.getAmount(), debtorToExecutorDebt);

        DebtClosure entity = DebtClosure.builder()
                .association(new TwoUsersAssociation(executorUser, debtorUser))
                .amount(createDto.getAmount())
                .createdAt(LocalDateTime.now())
                .build();

        validator.validate(entity);
        DebtClosure savedEntity = repository.save(entity);

        debtService.deductDebt(debtorUser.getId(), executorUser.getId(), createDto.getAmount());
        debtService.increaseHistoricalDebt(debtorUser.getId(), executorUser.getId(), createDto.getAmount());

        return mapper.toResponse(savedEntity);
    }

}