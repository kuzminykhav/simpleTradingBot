package io.exchange.backend.assignment.tradingbot.service.trade;

import io.exchange.backend.assignment.tradingbot.configuration.TradeProperties;
import io.exchange.backend.assignment.tradingbot.model.quote.FeedEvent;
import io.exchange.backend.assignment.tradingbot.model.quote.QuoteEventBody;
import io.exchange.backend.assignment.tradingbot.service.feed.EventFeedService;
import io.exchange.backend.assignment.tradingbot.statemachine.event.TradeEvents;
import io.exchange.backend.assignment.tradingbot.statemachine.state.TradeState;
import io.exchange.backend.assignment.tradingbot.statemachine.variable.TradeVariable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * Trade bot service implementation. Infra for trading
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class TradeServiceImpl implements TradeService {

    private final StateMachine<TradeState, TradeEvents> stateMachine;
    private final EventFeedService eventFeedService;
    private final TradeProperties tradeProperties;

    /**
     * Service for make trade with predefined prices, limits and productId
     * Using {@link io.exchange.backend.assignment.tradingbot.service.order.OrderService} and
     * {@link io.exchange.backend.assignment.tradingbot.statemachine.StateMachineConfig} StateMachine as
     * a trading engine for trade
     *
     * @return feed of quotes
     */
    @Override
    public Flux<QuoteEventBody> makeTrade() {

        stateMachine.getExtendedState().getVariables().put(TradeVariable.BUY_PRICE, tradeProperties.getBuyPrice());
        stateMachine.getExtendedState().getVariables().put(TradeVariable.TAKEPROFITE_PRICE, tradeProperties.getTakeProfitPrice());
        stateMachine.getExtendedState().getVariables().put(TradeVariable.STOPLOSS_PRICE, tradeProperties.getStopLossPrice());
        stateMachine.getExtendedState().getVariables().put(TradeVariable.PRODUCT_ID, tradeProperties.getProductId());


        return eventFeedService.getEvents(tradeProperties.getProductId())
                .map(FeedEvent::getBody)
                .ofType(QuoteEventBody.class)
                .filter(quote -> quote.getSecurityId().equals(tradeProperties.getProductId()))
                .doOnNext(s -> {
                    log.info("price " + s.getCurrentPrice());
                    stateMachine.getExtendedState().getVariables().put(TradeVariable.CURRENT_PRICE, new BigDecimal(s.getCurrentPrice()));
                    Mono.just(stateMachine.sendEvent(TradeEvents.QUOTE)).subscribe();
                });
    }
}
