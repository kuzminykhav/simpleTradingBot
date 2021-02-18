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

import java.math.BigDecimal;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BuyActionTest {

    private BuyAction buyAction;

    private final String testProductId = "testProductId";
    private final String errorTestProductId = "errorTestProductId";
    private final String testPositionId = "testPositionId";
    private final Trade testTrade = Trade.builder().positionId(testPositionId).build();


    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private StateContext<TradeState, TradeEvents> context;

    @Mock
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        buyAction = new BuyAction(orderService);
    }

    @Test
    void givenMachineContext_whenExecute_thenSuccessOrderServiceOpen() {
        //given
        when(context.getStateMachine().getExtendedState().getVariables().put(TradeVariable.POSITION_ID, testPositionId)).thenReturn(testPositionId);
        when(context.getExtendedState().get(TradeVariable.PRODUCT_ID, String.class)).thenReturn(testProductId);
        when(orderService.openOrder(testProductId, BigDecimal.TEN)).thenReturn(Mono.just(testTrade));
        //when
        buyAction.execute(context);
        //then
        verify(orderService, times(1)).openOrder(testProductId, BigDecimal.TEN);
        verify(context.getStateMachine().getExtendedState().getVariables(), times(1)).put(TradeVariable.POSITION_ID, testPositionId);
        verify(context.getStateMachine(), times(1)).sendEvent(TradeEvents.HOLD);
    }

    @Test
    void givenContextMachineContext_whenExecute_thenErroredOrderServiceOpen() {
        //given
        when(context.getExtendedState().get(TradeVariable.PRODUCT_ID, String.class)).thenReturn(errorTestProductId);
        doReturn(Mono.error(Exception::new)).when(orderService).openOrder(errorTestProductId, BigDecimal.TEN);
        //when
        buyAction.execute(context);
        //then
        verify(orderService, times(1)).openOrder(errorTestProductId, BigDecimal.TEN);
        verify(context.getStateMachine(), times(1)).sendEvent(TradeEvents.ERROR);
    }
}