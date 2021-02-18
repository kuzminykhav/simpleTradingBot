package io.exchange.backend.assignment.tradingbot.service.feed;

import io.exchange.backend.assignment.tradingbot.model.quote.FeedEvent;
import reactor.core.publisher.Flux;

/**
 * Service provides stream of events
 */

public interface EventFeedService {

    /**
     * @param productId id of product for subscription
     * @return Flux of events subscription to this product
     */
    Flux<FeedEvent> getEvents(String productId);
}
