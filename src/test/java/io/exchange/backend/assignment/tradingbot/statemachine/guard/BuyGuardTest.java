package io.exchange.backend.assignment.tradingbot.statemachine.guard;

import io.exchange.backend.assignment.tradingbot.statemachine.event.TradeEvents;
import io.exchange.backend.assignment.tradingbot.statemachine.state.TradeState;
import io.exchange.backend.assignment.tradingbot.statemachine.variable.TradeVariable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.statemachine.StateContext;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuyGuardTest {

    private final BuyGuard buyGuard = new BuyGuard();

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private StateContext<TradeState, TradeEvents> context;

    @Test
    void givenMachineContextCurrentPriceLessBuyPrice_whenEvaluate_thenReturnFalse() {
        //given
        when(context.getExtendedState().get(TradeVariable.CURRENT_PRICE, BigDecimal.class)).thenReturn(BigDecimal.ZERO);
        when(context.getExtendedState().get(TradeVariable.BUY_PRICE, BigDecimal.class)).thenReturn(BigDecimal.ONE);
        //when
        //then
        assertFalse(buyGuard.evaluate(context));
    }

    @Test
    void givenMachineContextCurrentPriceMoreBuyPrice_whenEvaluate_thenReturnFalse() {
        //given
        when(context.getExtendedState().get(TradeVariable.CURRENT_PRICE, BigDecimal.class)).thenReturn(BigDecimal.TEN);
        when(context.getExtendedState().get(TradeVariable.BUY_PRICE, BigDecimal.class)).thenReturn(BigDecimal.ZERO);
        //when
        //then
        assertFalse(buyGuard.evaluate(context));
    }


    @Test
    void givenMachineContextCurrentPriceEqualBuyPrice_whenEvaluate_thenReturnTrue() {
        //given
        when(context.getExtendedState().get(TradeVariable.CURRENT_PRICE, BigDecimal.class)).thenReturn(BigDecimal.ONE);
        when(context.getExtendedState().get(TradeVariable.BUY_PRICE, BigDecimal.class)).thenReturn(BigDecimal.ONE);
        //when
        //then
        assertTrue(buyGuard.evaluate(context));
    }

}