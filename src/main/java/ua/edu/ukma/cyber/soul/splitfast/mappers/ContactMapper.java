package ua.edu.ukma.cyber.soul.splitfast.mappers;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ContactDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ContactListDto;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.DebtInfoDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactEntity;

import java.util.List;

@Mapper(config = MapperConfiguration.class)
public abstract class ContactMapper {

    @Autowired
    protected UserMapper userMapper;

    @Mapping(target = "current", source = "firstCurrentDebt")
    @Mapping(target = "historical", source = "firstHistoricalDebt")
    public abstract DebtInfoDto toFirstUserDebt(ContactEntity contact);

    @Mapping(target = "current", source = "secondCurrentDebt")
    @Mapping(target = "historical", source = "secondHistoricalDebt")
    public abstract DebtInfoDto toSecondUserDebt(ContactEntity contact);

    @Mapping(target = "thisUserId", expression = "java(thisUserId)")
    @Mapping(
            target = "thisUserDebtInfo",
            expression = "java(thisUserId == entity.getUsersAssociation().getFirstUserId() ? toFirstUserDebt(entity) : toSecondUserDebt(entity))"
    )
    @Mapping(
            target = "otherUser",
            expression = "java(userMapper.toShortResponse(entity.getUsersAssociation().getFirstUserId() == thisUserId ? entity.getSecondUser() : entity.getFirstUser()))"
    )
    @Mapping(
            target = "otherUserDebtInfo",
            expression = "java(thisUserId == entity.getUsersAssociation().getFirstUserId() ? toSecondUserDebt(entity) : toFirstUserDebt(entity))"
    )
    @Mapping(
            target = "isMarked",
            expression = "java(thisUserId == entity.getUsersAssociation().getFirstUserId() ? entity.isFirstIsMarked() : entity.isSecondIsMarked())"
    )
    public abstract ContactDto toResponse(@Context int thisUserId, ContactEntity entity);

    public abstract ContactListDto toListResponse(@Context int thisUserId, long total, List<ContactEntity> items);
}
