package io.exchange.backend.assignment.tradingbot.service.trade;

import io.exchange.backend.assignment.tradingbot.model.quote.QuoteEventBody;
import reactor.core.publisher.Flux;

/**
 * Trade bot service. Infra for trading
 */
public interface TradeService {

    /**
     * Service for make trade with predefined prices, limits and productId
     *
     * @return Flux<FeedEvent> of quotes
     */
    Flux<QuoteEventBody> makeTrade();
}
