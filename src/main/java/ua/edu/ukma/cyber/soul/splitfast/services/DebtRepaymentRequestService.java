package ua.edu.ukma.cyber.soul.splitfast.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.CreateDebtRepaymentRequestDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtRepaymentRequestCriteriaDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtRepaymentRequestDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtRepaymentRequestListDto;
import ua.edu.ukma.cyber.soul.splitfast.criteria.DebtRepaymentRequestCriteria;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.DebtRepaymentRequest;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.helpers.TwoUsersAssociation;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ForbiddenException;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.NotFoundException;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.repositories.CriteriaRepository;
import ua.edu.ukma.cyber.soul.splitfast.repositories.UserRepository;
import ua.edu.ukma.cyber.soul.splitfast.security.SecurityUtils;


import ua.edu.ukma.cyber.soul.splitfast.mappers.DebtRepaymentRequestMapper;
import ua.edu.ukma.cyber.soul.splitfast.repositories.DebtRepaymentRequestRepository;
import ua.edu.ukma.cyber.soul.splitfast.validators.DebtRepaymentRequestValidator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DebtRepaymentRequestService {

    private final DebtRepaymentRequestRepository repository;
    private final UserRepository userRepository;
    private final DebtRepaymentRequestMapper mapper;
    private final DebtRepaymentRequestValidator validator;
    private final SecurityUtils securityUtils;
    private final DebtService debtService;
    private final CriteriaRepository criteriaRepository;

    @Transactional
    public DebtRepaymentRequestDto createDebtRepaymentRequest(CreateDebtRepaymentRequestDto createDto) {
        UserEntity fromUser = securityUtils.getCurrentUser();
        UserEntity toUser = userRepository.findById(createDto.getToUserId())
                .orElseThrow(() -> new NotFoundException(UserEntity.class, "id: " + createDto.getToUserId()));

        BigDecimal currentDebt = debtService.getDebtAmount(fromUser.getId(), toUser.getId());
        BigDecimal unsubmittedRequestsSum = repository.sumUnsubmittedRequests(fromUser.getId(), toUser.getId());
        BigDecimal totalAvailableForRequest = currentDebt.subtract(unsubmittedRequestsSum);

        validator.validateDebtRepaymentRequestAmount(createDto.getAmount(), totalAvailableForRequest);

        DebtRepaymentRequest entity = DebtRepaymentRequest.builder()
                .association(new TwoUsersAssociation(fromUser, toUser))
                .createdAt(LocalDateTime.now())
                .amount(createDto.getAmount())
                .submitted(false)
                .build();

        validator.validate(entity);
        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public void submitDebtRepaymentRequest(Integer id) {
        DebtRepaymentRequest request = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(DebtRepaymentRequest.class, "id: " + id));

        UserEntity currentUser = securityUtils.getCurrentUser();

        if (!request.getAssociation().getFirstUser().getId().equals(currentUser.getId()) &&
                !request.getAssociation().getSecondUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenException();
        }

        Integer fromUserId = request.getAssociation().getFirstUserId();
        Integer toUserId = request.getAssociation().getSecondUserId();

        if (!currentUser.getId().equals(fromUserId)) {
            throw new ForbiddenException();
        }

        if (request.isSubmitted()) {
            throw new ValidationException("error.debt-request.already-sumbited");
        }

        request.setSubmitted(true);
        request.setSubmittedAt(LocalDateTime.now());
        repository.save(request);

        debtService.deductDebt(fromUserId, toUserId, request.getAmount());
        debtService.increaseHistoricalDebt(fromUserId, toUserId, request.getAmount());

        BigDecimal fromTo = debtService.getDebtAmount(fromUserId, toUserId);
        BigDecimal toFrom = debtService.getDebtAmount(toUserId, fromUserId);
        BigDecimal effectiveDebt = fromTo.subtract(toFrom);

        if (effectiveDebt.compareTo(BigDecimal.ZERO) == 0) {
            debtService.transferAllCurrentToHistoricalDebt(fromUserId, toUserId);
        }
    }


    @Transactional(readOnly = true)
    public DebtRepaymentRequestListDto getDebtRepaymentRequestsByCriteria(DebtRepaymentRequestCriteriaDto criteriaDto) {
        DebtRepaymentRequestCriteria criteria = new DebtRepaymentRequestCriteria(criteriaDto);
        List<DebtRepaymentRequest> entities = criteriaRepository.find(criteria);
        validator.validForView(entities);
        long total = criteriaRepository.count(criteria);
        return mapper.toListDto(total, entities);
    }
}