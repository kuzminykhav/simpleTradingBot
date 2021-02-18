package io.exchange.backend.assignment.tradingbot.statemachine.guard;

import io.exchange.backend.assignment.tradingbot.statemachine.event.TradeEvents;
import io.exchange.backend.assignment.tradingbot.statemachine.state.TradeState;
import io.exchange.backend.assignment.tradingbot.statemachine.variable.TradeVariable;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SellGuard implements Guard<TradeState, TradeEvents> {
    @Override
    public boolean evaluate(StateContext<TradeState, TradeEvents> stateContext) {
        BigDecimal currentPrice = stateContext.getExtendedState().get(TradeVariable.CURRENT_PRICE, BigDecimal.class);
        BigDecimal stopLossPrice = stateContext.getExtendedState().get(TradeVariable.STOPLOSS_PRICE, BigDecimal.class);
        BigDecimal takeProfitPrice = stateContext.getExtendedState().get(TradeVariable.TAKEPROFITE_PRICE, BigDecimal.class);
        return currentPrice.compareTo(stopLossPrice) <= 0 || currentPrice.compareTo(takeProfitPrice) >= 0;
    }
}
