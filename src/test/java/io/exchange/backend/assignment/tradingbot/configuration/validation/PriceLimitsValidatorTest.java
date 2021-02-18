package io.exchange.backend.assignment.tradingbot.configuration.validation;

import io.exchange.backend.assignment.tradingbot.configuration.TradeProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PriceLimitsValidatorTest {

    private Validator validator;


    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void givenBotProperties_whenValidate_thenSuccessBuyPriceLessThanTakeProfitLimitAndMoreThanStopLoss() {
        //given
        TradeProperties tradeProperties = new TradeProperties(BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO, "testProductId", "token", "url","21");
        //when
        Set<ConstraintViolation<TradeProperties>> violations = validator.validate(tradeProperties);
        //then
        assertTrue(violations.isEmpty());
    }


    @Test
    void givenBotProperties_whenValidate_thenFailedBuyPriceMoreThanTakeProfitLimitAndMoreThanStopLoss() {
        //given
        TradeProperties tradeProperties = new TradeProperties(BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO, "testProductId", "token", "url","21");
        //when
        Set<ConstraintViolation<TradeProperties>> violations = validator.validate(tradeProperties);
        //then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("buy price should be in between sell price and upper limit sell price", violations.stream().map(ConstraintViolation::getMessage).findFirst().get());
    }

    @Test
    void givenBotProperties_whenValidate_thenFailedBuyPriceLessThanTakeProfitLimitAndLessThanStopLoss() {
        //given
        TradeProperties tradeProperties = new TradeProperties(BigDecimal.TEN, BigDecimal.ONE, BigDecimal.TEN, "testProductId", "token", "url","21");
        //when
        Set<ConstraintViolation<TradeProperties>> violations = validator.validate(tradeProperties);
        //then
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("buy price should be in between sell price and upper limit sell price", violations.stream().map(ConstraintViolation::getMessage).findFirst().get());
    }
}