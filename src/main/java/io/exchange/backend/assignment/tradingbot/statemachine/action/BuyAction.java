package io.exchange.backend.assignment.tradingbot.statemachine.action;

import io.exchange.backend.assignment.tradingbot.service.order.OrderService;
import io.exchange.backend.assignment.tradingbot.statemachine.event.TradeEvents;
import io.exchange.backend.assignment.tradingbot.statemachine.state.TradeState;
import io.exchange.backend.assignment.tradingbot.statemachine.variable.TradeVariable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class BuyAction implements Action<TradeState, TradeEvents> {

    private final OrderService orderService;

    public BuyAction(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void execute(final StateContext<TradeState, TradeEvents> context) {
        final String productId = context.getExtendedState().get(TradeVariable.PRODUCT_ID, String.class);
        orderService.openOrder(productId, BigDecimal.TEN)
                .doOnSuccess(s -> {
                    context.getStateMachine().getExtendedState().getVariables().put(TradeVariable.POSITION_ID, s.getPositionId());
                    context.getStateMachine().sendEvent(TradeEvents.HOLD);
                })
                .doOnError(s -> context.getStateMachine().sendEvent(TradeEvents.ERROR))
                .subscribe();
        log.info("BuyAction executed for " + productId);
    }
}
