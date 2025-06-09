package ua.edu.ukma.cyber.soul.splitfast.mergers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.CreateExpenseMemberDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UpdateExpenseMemberDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ExpenseMemberEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;
import ua.edu.ukma.cyber.soul.splitfast.exceptions.ValidationException;
import ua.edu.ukma.cyber.soul.splitfast.mappers.EnumsMapper;
import ua.edu.ukma.cyber.soul.splitfast.repositories.UserRepository;

@Component
@RequiredArgsConstructor
public class ExpenseMemberMerger implements IMerger<ExpenseMemberEntity, CreateExpenseMemberDto, UpdateExpenseMemberDto> {

    private final UserRepository userRepository;
    private final EnumsMapper enumsMapper;

    @Override
    public void mergeForCreate(ExpenseMemberEntity entity, CreateExpenseMemberDto view) {
        UserEntity user = userRepository.findById(view.getUserId())
                .orElseThrow(() -> new ValidationException("error.expense-member.user.not-exists"));
        entity.setUser(user);
        entity.setUserId(user.getId());
        entity.setType(enumsMapper.map(view.getType()));
        merge(entity, view);
    }

    @Override
    public void mergeForUpdate(ExpenseMemberEntity entity, UpdateExpenseMemberDto view) {
        merge(entity, view);
    }

    private void merge(ExpenseMemberEntity entity, UpdateExpenseMemberDto view) {
        entity.setShare(view.getShare());
    }
}
