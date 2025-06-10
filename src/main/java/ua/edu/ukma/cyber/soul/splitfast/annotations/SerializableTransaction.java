package ua.edu.ukma.cyber.soul.splitfast.annotations;

import org.springframework.core.annotation.AliasFor;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Retryable(
        retryFor = ConcurrencyFailureException.class,
        maxAttemptsExpression = "${concurrency.serialization-failure.retry.max-attempts}",
        backoff = @Backoff(
                delayExpression = "${concurrency.serialization-failure.retry.delay}",
                multiplierExpression = "${concurrency.serialization-failure.retry.multiplier}"
        )
)
@Transactional(isolation = Isolation.SERIALIZABLE)
public @interface SerializableTransaction {

    @AliasFor(annotation =  Transactional.class, attribute = "readOnly")
    boolean readOnly() default false;
}
