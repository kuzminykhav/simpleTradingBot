package io.exchange.backend.assignment.tradingbot.configuration.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation for validating input properties lower limit sell price < buy price
 * < upper limit sell price.
 * Uses PriceLimitsValidator {@link io.exchange.backend.assignment.tradingbot.configuration.validate.PriceLimitsValidator}
 * for validation
 */

@Constraint(validatedBy = PriceLimitsValidator.class)
@Target({TYPE})
@Retention(RUNTIME)
@Documented
public @interface ConsistentPriceLimits {

    String message() default "{io.exchange.backend.assignment.tradingbot.configuration.validate.ConsistentPriceLimits.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
