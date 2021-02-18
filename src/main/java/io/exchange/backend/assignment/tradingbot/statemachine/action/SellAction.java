package io.exchange.backend.assignment.tradingbot.statemachine.action;

import io.exchange.backend.assignment.tradingbot.service.order.OrderService;
import io.exchange.backend.assignment.tradingbot.statemachine.event.TradeEvents;
import io.exchange.backend.assignment.tradingbot.statemachine.state.TradeState;
import io.exchange.backend.assignment.tradingbot.statemachine.variable.TradeVariable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SellAction implements Action<TradeState, TradeEvents> {

    private final OrderService orderService;

    public SellAction(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void execute(final StateContext<TradeState, TradeEvents> context) {
        final String positionId = context.getExtendedState().get(TradeVariable.POSITION_ID, String.class);
        orderService.closeOrder(positionId)
                .doOnSuccess(c -> context.getStateMachine().sendEvent(TradeEvents.SOLD))
                .doOnError(s -> context.getStateMachine().sendEvent(TradeEvents.ERROR))
                .subscribe();
        log.info("SellAction executed for podsition  {}", positionId);
    }
}
