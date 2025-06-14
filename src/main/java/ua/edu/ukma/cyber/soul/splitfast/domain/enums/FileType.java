package ua.edu.ukma.cyber.soul.splitfast.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ActivitiesGroupEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ExpenseEntity;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.UserEntity;

@Getter
@RequiredArgsConstructor
public enum FileType {
    USER_AVATAR(true, UserEntity.class),
    ACTIVITIES_GROUP_AVATAR(true, ActivitiesGroupEntity.class),
    EXPENSE_ATTACHMENT(false, ExpenseEntity.class);

    private final boolean isUnique;
    private final Class<?> entityClass;
}
