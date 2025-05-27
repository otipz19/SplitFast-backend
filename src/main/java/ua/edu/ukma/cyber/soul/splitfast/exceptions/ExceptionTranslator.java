package ua.edu.ukma.cyber.soul.splitfast.exceptions;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ua.edu.ukma.cyber.soul.splitfast.controllers.rest.model.ErrorResponseDto;

import java.util.*;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ExceptionTranslator {
    
    private final MessageSource messageSource;
    private final TranslatorsMap translators = new TranslatorsMap(t -> toBaseResponse("error.application.unexpected"))
        .add(NoResourceFoundException.class, t -> toBaseResponse("error.application.no-resource"))
        .add(HttpRequestMethodNotSupportedException.class, t -> toBaseResponse("error.application.method-not-allowed"))
        .add(MethodArgumentNotValidException.class, t -> {
            ErrorResponseDto errorResponseDto = toBaseResponse("error.application.invalid-data");
            List<String> details = t.getBindingResult().getFieldErrors().stream()
                    .map(FieldError::getCode)
                    .filter(Objects::nonNull)
                    .map(this::translateMessage)
                    .toList();
            errorResponseDto.setDetails(details);
            return errorResponseDto;
        })
        .add(ForbiddenException.class, this::toBaseResponse)
        .add(NotFoundException.class, t -> {
            ErrorResponseDto errorResponseDto = toBaseResponse(t);
            if (t.getDetails() != null)
                errorResponseDto.setDetails(List.of(t.getDetails()));
            return errorResponseDto;
        })
        .add(ValidationException.class, t -> {
            ErrorResponseDto errorResponseDto = toBaseResponse(t);
            List<String> details = t.getViolations().stream()
                    .map(v -> translateMessage(v.getMessage()))
                    .toList();
            errorResponseDto.setDetails(details);
            return errorResponseDto;
        });

    @SuppressWarnings("unchecked")
    public <T extends Throwable> ErrorResponseDto translate(T exception) {
        return translators.get((Class<T>) exception.getClass()).apply(exception);
    }

    private ErrorResponseDto toBaseResponse(Throwable exception) {
        return toBaseResponse(exception.getMessage());
    }

    private ErrorResponseDto toBaseResponse(String message) {
        return new ErrorResponseDto(translateMessage(message), new ArrayList<>());
    }

    private String translateMessage(String message) {
        return messageSource.getMessage(message, null, Locale.ROOT);
    }

    private static class TranslatorsMap {

        private final Map<Class<? extends Throwable>, Function<?, ErrorResponseDto>> map = new HashMap<>();
        private final Function<Throwable, ErrorResponseDto> defaultTranslator;

        public TranslatorsMap(Function<Throwable, ErrorResponseDto> defaultTranslator) {
            this.defaultTranslator = defaultTranslator;
        }

        public <T extends Throwable> TranslatorsMap add(Class<T> exceptionClass, Function<T, ErrorResponseDto> translator) {
            map.put(exceptionClass, translator);
            return this;
        }

        @SuppressWarnings("unchecked")
        public <T extends Throwable> Function<T, ErrorResponseDto> get(Class<T> exceptionClass) {
            return (Function<T, ErrorResponseDto>) map.getOrDefault(exceptionClass, defaultTranslator);
        }
    }
}
