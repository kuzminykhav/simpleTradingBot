package io.exchange.backend.assignment.tradingbot.statemachine;

import io.exchange.backend.assignment.tradingbot.statemachine.event.TradeEvents;
import io.exchange.backend.assignment.tradingbot.statemachine.guard.BuyGuard;
import io.exchange.backend.assignment.tradingbot.statemachine.guard.SellGuard;
import io.exchange.backend.assignment.tradingbot.statemachine.state.TradeState;
import io.exchange.backend.assignment.tradingbot.statemachine.action.BuyAction;
import io.exchange.backend.assignment.tradingbot.statemachine.action.SellAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

import static io.exchange.backend.assignment.tradingbot.statemachine.event.TradeEvents.*;
import static io.exchange.backend.assignment.tradingbot.statemachine.state.TradeState.*;

@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<TradeState, TradeEvents> {

    @Autowired
    SellAction sellAction;

    @Autowired
    BuyAction buyAction;

    @Autowired
    BuyGuard buyGuard;

    @Autowired
    SellGuard sellGuard;

    @Override
    public void configure(final StateMachineStateConfigurer<TradeState, TradeEvents> states) throws Exception {
        states
                .withStates()
                .initial(NEW)
                .end(COMPLETE)
                .states(EnumSet.allOf(TradeState.class));

    }

    @Override
    public void configure(final StateMachineConfigurationConfigurer<TradeState, TradeEvents> config) throws Exception {
        config
                .withConfiguration()
                .autoStartup(true);
    }

    @Override
    public void configure(final StateMachineTransitionConfigurer<TradeState, TradeEvents> transitions) throws Exception {
        transitions
                .withExternal()
                    .source(NEW)
                    .target(TRADE_BUY)
                    .event(QUOTE)
                    .guard(buyGuard)
                    .action(buyAction)
                .and()
                .withExternal()
                    .source(TRADE_BUY)
                    .target(NEW)
                    .event(ERROR)
                .and()
                .withExternal()
                    .source(TRADE_BUY)
                    .target(HOLDING)
                    .event(HOLD)
                .and()
                .withExternal()
                    .source(HOLDING)
                    .target(TRADE_SELL)
                    .event(QUOTE)
                    .guard(sellGuard)
                    .action(sellAction)
                .and()
                .withExternal()
                    .source(TRADE_SELL)
                    .target(HOLDING)
                    .event(ERROR)
                .and()
                .withExternal()
                    .source(TRADE_SELL)
                    .target(NEW)
                    .event(SOLD);
    }

}
