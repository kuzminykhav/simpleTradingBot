package io.exchange.backend.assignment.tradingbot.shell;

import io.exchange.backend.assignment.tradingbot.service.trade.TradeService;
import io.exchange.backend.assignment.tradingbot.statemachine.event.TradeEvents;
import io.exchange.backend.assignment.tradingbot.statemachine.state.TradeState;
import io.exchange.backend.assignment.tradingbot.statemachine.variable.TradeVariable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.statemachine.StateMachine;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TradeCommandsTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private StateMachine<TradeState, TradeEvents> stateMachine;
    @Mock
    private TradeService tradeService;

    private TradeCommands tradeCommands;

    @BeforeEach
    void setUp() {
        tradeCommands = new TradeCommands(stateMachine, tradeService);
    }

//    @Test
//    void givenTradeCommands_whenConnect_therReturnConnect() {
//        //given
//        when(tradeService.makeTrade()).thenReturn(Flux.just(new QuoteEventBody()));
//        //when then
//        assertEquals("connected", tradeCommands.connect());
//        verify(tradeService, times(1)).makeTrade();
//    }

    @Test
    void givenTradeCommands_whenSetBuyPrice_therReturnConnect() {
        //given
        //when then
        assertEquals("new price is " + BigDecimal.TEN, tradeCommands.setbuyprice(BigDecimal.TEN));
        verify(stateMachine.getExtendedState().getVariables(), times(1)).put(TradeVariable.BUY_PRICE, BigDecimal.TEN);
    }

    @Test
    void givenTradeCommands_whenSetStopLimitPrice_therReturnConnect() {
        //given
        //when then
        assertEquals("new stop loss limit is " + BigDecimal.TEN, tradeCommands.setstoplosslimit(BigDecimal.TEN));
        verify(stateMachine.getExtendedState().getVariables(), times(1)).put(TradeVariable.STOPLOSS_PRICE, BigDecimal.TEN);
    }

    @Test
    void givenTradeCommands_whenSetTakeProfitLimitPrice_therReturnConnect() {
        //given
        //when then
        assertEquals("new take profit limit is " + BigDecimal.TEN, tradeCommands.settakeprofitlimit(BigDecimal.TEN));
        verify(stateMachine.getExtendedState().getVariables(), times(1)).put(TradeVariable.TAKEPROFITE_PRICE, BigDecimal.TEN);
    }
}