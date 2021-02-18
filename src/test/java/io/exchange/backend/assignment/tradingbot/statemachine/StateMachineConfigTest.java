package io.exchange.backend.assignment.tradingbot.statemachine;

import io.exchange.backend.assignment.tradingbot.statemachine.action.BuyAction;
import io.exchange.backend.assignment.tradingbot.statemachine.action.SellAction;
import io.exchange.backend.assignment.tradingbot.statemachine.event.TradeEvents;
import io.exchange.backend.assignment.tradingbot.statemachine.guard.BuyGuard;
import io.exchange.backend.assignment.tradingbot.statemachine.guard.SellGuard;
import io.exchange.backend.assignment.tradingbot.statemachine.state.TradeState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.test.StateMachineTestPlan;
import org.springframework.statemachine.test.StateMachineTestPlanBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {StateMachineConfig.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StateMachineConfigTest {

    @Autowired
    private StateMachine<TradeState, TradeEvents> stateMachineConfig;

    @MockBean
    SellAction sellAction;

    @MockBean
    BuyAction buyAction;

    @MockBean
    BuyGuard buyGuard;

    @MockBean
    SellGuard sellGuard;

    @Test
    public void givenNewStateMachine_whenCheckInitialState_thenReturnNEW() throws Exception {
        StateMachineTestPlan<TradeState, TradeEvents> plan =
                StateMachineTestPlanBuilder.<TradeState, TradeEvents>builder()
                        .stateMachine(stateMachineConfig)
                        .step()
                        .expectState(TradeState.NEW)
                        .and()
                        .build();
        plan.test();
    }

    @Test
    public void givenNewStateMachine_whenSendQUOTE_thenReturnStillNEWtate() throws Exception {
        StateMachineTestPlan<TradeState, TradeEvents> plan =
                StateMachineTestPlanBuilder.<TradeState, TradeEvents>builder()
                        .stateMachine(stateMachineConfig)
                        .step()
                            .expectState(TradeState.NEW)
                            .and()
                        .step()
                            .sendEvent(TradeEvents.QUOTE)
                            .expectState(TradeState.NEW)
                            .and()
                        .build();
        plan.test();
    }

    @Test
    public void givenConnectedStateMachineBuyGuardReturnsTrue_whenSendQUOTEHOLD_thenReturnHOLDINGState() throws Exception {
        when(buyGuard.evaluate(any())).thenReturn(true);
        StateMachineTestPlan<TradeState, TradeEvents> plan =
                StateMachineTestPlanBuilder.<TradeState, TradeEvents>builder()
                        //given
                        .stateMachine(stateMachineConfig)
                        .step()
                            .expectState(TradeState.NEW)
                            .and()
                        //when BUY action works
                        .step()
                            .sendEvent(TradeEvents.QUOTE)
                            .expectState(TradeState.TRADE_BUY)
                            .and()
                        .step()
                            .sendEvent(TradeEvents.HOLD)
                            .expectState(TradeState.HOLDING)
                            .and()
                        .build();
        plan.test();
    }

    @Test
    public void givenConnectedStateMachineBuyGuardReturnsTrue_whenSendQUOTEERROR_thenReturnNEWState() throws Exception {
        when(buyGuard.evaluate(any())).thenReturn(true);
        StateMachineTestPlan<TradeState, TradeEvents> plan =
                StateMachineTestPlanBuilder.<TradeState, TradeEvents>builder()
                        //given
                        .stateMachine(stateMachineConfig)
                        .step()
                        .expectState(TradeState.NEW)
                        .and()
                        //when BUY action works
                        .step()
                        .sendEvent(TradeEvents.QUOTE)
                        .expectState(TradeState.TRADE_BUY)
                        .and()
                        .step()
                        .sendEvent(TradeEvents.ERROR)
                        .expectState(TradeState.NEW)
                        .and()
                        .build();
        plan.test();
    }

    @Test
    public void givenHoldingStateMachineSellGuardReturnsTrue_whenSendQUOTEError_thenReturnHOLDState() throws Exception {
        when(sellGuard.evaluate(any())).thenReturn(true);
        when(buyGuard.evaluate(any())).thenReturn(true);
        StateMachineTestPlan<TradeState, TradeEvents> plan =
                StateMachineTestPlanBuilder.<TradeState, TradeEvents>builder()
                        //given
                        .stateMachine(stateMachineConfig)
                        .step()
                        .expectState(TradeState.NEW)
                        .and()
                        .step()
                        .sendEvent(TradeEvents.QUOTE)
                        .expectState(TradeState.TRADE_BUY)
                        .   and()
                        .step()
                        .sendEvent(TradeEvents.HOLD)
                        .expectState(TradeState.HOLDING)
                        .and()
                        //then sell action works
                        .step()
                        .sendEvent(TradeEvents.QUOTE)
                        .expectState(TradeState.TRADE_SELL)
                        .and()
                        .step()
                        .sendEvent(TradeEvents.ERROR)
                        .expectState(TradeState.HOLDING)
                        .and()
                        .build();
        plan.test();
    }

    @Test
    public void givenHoldingStateMachineSellGuardReturnsTrue_whenSendQUOTE_thenReturnNEWState() throws Exception {
        when(sellGuard.evaluate(any())).thenReturn(true);
        when(buyGuard.evaluate(any())).thenReturn(true);
        StateMachineTestPlan<TradeState, TradeEvents> plan =
                StateMachineTestPlanBuilder.<TradeState, TradeEvents>builder()
                        //given
                        .stateMachine(stateMachineConfig)
                            .step()
                            .expectState(TradeState.NEW)
                            .and()
                        .step()
                            .sendEvent(TradeEvents.QUOTE)
                            .expectState(TradeState.TRADE_BUY)
                        .   and()
                        .step()
                            .sendEvent(TradeEvents.HOLD)
                            .expectState(TradeState.HOLDING)
                        .and()
                        //then sell action works
                        .step()
                            .sendEvent(TradeEvents.QUOTE)
                            .expectState(TradeState.TRADE_SELL)
                            .and()
                        .step()
                            .sendEvent(TradeEvents.SOLD)
                            .expectState(TradeState.NEW)
                            .and()
                        .build();
        plan.test();
    }

}