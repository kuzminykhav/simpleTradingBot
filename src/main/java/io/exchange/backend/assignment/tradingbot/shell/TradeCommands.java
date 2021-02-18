package io.exchange.backend.assignment.tradingbot.shell;

import io.exchange.backend.assignment.tradingbot.service.trade.TradeService;
import io.exchange.backend.assignment.tradingbot.statemachine.event.TradeEvents;
import io.exchange.backend.assignment.tradingbot.statemachine.state.TradeState;
import io.exchange.backend.assignment.tradingbot.statemachine.variable.TradeVariable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.statemachine.StateMachine;

import java.math.BigDecimal;

/**
 * Created for testing and control
 */
@ShellComponent
@Slf4j
public class TradeCommands {

    private StateMachine<TradeState, TradeEvents> stateMachine;
    private TradeService tradeService;

    public TradeCommands(StateMachine<TradeState, TradeEvents> stateMachine, TradeService tradeService) {
        this.stateMachine = stateMachine;
        this.tradeService = tradeService;
    }


    @ShellMethod("Connect to exchange quotes feed")
    public String connect() {
        tradeService.makeTrade().subscribe();
        return "connected";
    }

    @ShellMethod("Set new buy price")
    public String setbuyprice(@ShellOption BigDecimal newBuyPrice) {
        stateMachine.getExtendedState().getVariables().put(TradeVariable.BUY_PRICE, newBuyPrice);
        return "new price is " + newBuyPrice.toString();
    }

    @ShellMethod("Set new stop loss price")
    public String setstoplosslimit(@ShellOption BigDecimal newStopLossPrice) {
        stateMachine.getExtendedState().getVariables().put(TradeVariable.STOPLOSS_PRICE, newStopLossPrice);
        return "new stop loss limit is " + newStopLossPrice.toString();
    }

    @ShellMethod("Set new buy price")
    public String settakeprofitlimit(@ShellOption BigDecimal newTakeProfitPrice) {
        stateMachine.getExtendedState().getVariables().put(TradeVariable.TAKEPROFITE_PRICE, newTakeProfitPrice);
        return "new take profit limit is " + newTakeProfitPrice.toString();
    }


}
