package io.exchange.backend.assignment.tradingbot.statemachine.guard;

import io.exchange.backend.assignment.tradingbot.statemachine.event.TradeEvents;
import io.exchange.backend.assignment.tradingbot.statemachine.state.TradeState;
import io.exchange.backend.assignment.tradingbot.statemachine.variable.TradeVariable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class BuyGuard implements Guard<TradeState, TradeEvents> {
    @Override
    public boolean evaluate(StateContext<TradeState, TradeEvents> stateContext) {
        BigDecimal currentPrice = stateContext.getExtendedState().get(TradeVariable.CURRENT_PRICE, BigDecimal.class);
        BigDecimal buyPrice = stateContext.getExtendedState().get(TradeVariable.BUY_PRICE, BigDecimal.class);
        //TODO could be in between BUY_PRICE and STOP_LOSS_PRICE
        return currentPrice.compareTo(buyPrice) == 0;
    }
}
