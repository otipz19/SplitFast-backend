package ua.edu.ukma.cyber.soul.splitfast.mergers;

import org.springframework.stereotype.Component;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.UpdateContactDto;
import ua.edu.ukma.cyber.soul.splitfast.domain.entitites.ContactEntity;

@Component
public class ContactMerger implements IMerger<ContactEntity, UpdateContactDto> {

    @Override
    public void mergeForCreate(ContactEntity entity, UpdateContactDto view) {
        merge(entity, view);
    }

    @Override
    public void mergeForUpdate(ContactEntity entity, UpdateContactDto view) {
        merge(entity, view);
    }

    private void merge(ContactEntity entity, UpdateContactDto view) {
        entity.setFirstCurrentDebt(view.getFirstCurrentDebt().doubleValue());
        entity.setSecondCurrentDebt(view.getSecondCurrentDebt().doubleValue());
        entity.setFirstHistoryDebt(view.getFirstHistoryDebt().doubleValue());
        entity.setSecondHistoryDebt(view.getSecondHistoryDebt().doubleValue());
    }
}
