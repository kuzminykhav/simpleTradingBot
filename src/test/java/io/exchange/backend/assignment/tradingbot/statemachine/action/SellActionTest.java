package io.exchange.backend.assignment.tradingbot.statemachine.action;

import io.exchange.backend.assignment.tradingbot.model.order.Trade;
import io.exchange.backend.assignment.tradingbot.service.order.OrderService;
import io.exchange.backend.assignment.tradingbot.statemachine.event.TradeEvents;
import io.exchange.backend.assignment.tradingbot.statemachine.state.TradeState;
import io.exchange.backend.assignment.tradingbot.statemachine.variable.TradeVariable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.statemachine.StateContext;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SellActionTest {

    private final String testPositionId = "testPositionId";
    private final String errorTestPositionId = "errorTestPositionId";
    private final Trade testTrade = Trade.builder().positionId(testPositionId).build();


    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private StateContext<TradeState, TradeEvents> context;

    @Mock
    private OrderService orderService;

    private SellAction sellAction;

    @BeforeEach
    void setUp() {
        sellAction = new SellAction(orderService);
    }

    @Test
    void givenMachineContext_whenExecute_thenSuccessOrderServiceClose() {
        //given
        when(context.getExtendedState().get(TradeVariable.POSITION_ID, String.class)).thenReturn(testPositionId);
        when(orderService.closeOrder(testPositionId)).thenReturn(Mono.just(testTrade));
        //then
        sellAction.execute(context);
        //verify
        verify(orderService, times(1)).closeOrder(testPositionId);
        verify(context.getStateMachine(), times(1)).sendEvent(TradeEvents.SOLD);
    }

    @Test
    void givenMachineContext_whenExecute_thenErroredOrderServiceClose() {
        //given
        when(context.getExtendedState().get(TradeVariable.POSITION_ID, String.class)).thenReturn(errorTestPositionId);
        doReturn(Mono.error(Exception::new)).when(orderService).closeOrder(errorTestPositionId);
        //then
        sellAction.execute(context);
        //verify
        verify(orderService, times(1)).closeOrder(errorTestPositionId);
        verify(context.getStateMachine(), times(1)).sendEvent(TradeEvents.ERROR);
    }
}