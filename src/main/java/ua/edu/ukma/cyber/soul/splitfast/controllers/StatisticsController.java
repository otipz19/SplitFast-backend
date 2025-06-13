package ua.edu.ukma.cyber.soul.splitfast.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.api.StatisticsControllerApi;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtDistributionDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.HonoraryUsersDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.StatisticsLevelDto;
import ua.edu.ukma.cyber.soul.splitfast.services.StatisticsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatisticsController implements StatisticsControllerApi {

    private final StatisticsService service;

    @Override
    public ResponseEntity<List<DebtDistributionDto>> getDebtDistribution(StatisticsLevelDto statisticsLevel, Integer aggregateId) {
        return ResponseEntity.ok(service.getDebtDistribution(statisticsLevel, aggregateId));
    }

    @Override
    public ResponseEntity<HonoraryUsersDto> getHonoraryUsers(StatisticsLevelDto statisticsLevel, Integer aggregateId) {
        return ResponseEntity.ok(service.getHonoraryUsers(statisticsLevel, aggregateId));
    }
}
