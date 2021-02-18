package io.exchange.backend.assignment.tradingbot.configuration.validate;

import io.exchange.backend.assignment.tradingbot.configuration.TradeProperties;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator for properties, validating input properties lower limit sell price < buy price < upper limit sell price.
 */

public class PriceLimitsValidator implements
        ConstraintValidator<ConsistentPriceLimits, TradeProperties> {
    @Override
    public void initialize(ConsistentPriceLimits constraintAnnotation) {
    }

    /**
     *
     * @param tradeProperties Input properties buyPrice,takeProfitPrice,stopLossPrice
     * @param constraintValidatorContext
     * @return result of validation
     */
    @Override
    public boolean isValid(TradeProperties tradeProperties, ConstraintValidatorContext constraintValidatorContext) {
        return tradeProperties.getBuyPrice().compareTo(tradeProperties.getTakeProfitPrice()) < 0
                && tradeProperties.getBuyPrice().compareTo(tradeProperties.getStopLossPrice()) > 0;
    }
}
