package io.exchange.backend.assignment.tradingbot.service.order;

import io.exchange.backend.assignment.tradingbot.model.order.Trade;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * Service for create and close order
 */
public interface OrderService {

    /**
     * @param productId
     * @param amount
     * @return Mono of {@link io.exchange.backend.assignment.tradingbot.model.order.Trade}
     */
    Mono<Trade> openOrder(String productId, BigDecimal amount);

    /**
     * @param positionId ID of position for closing
     * @return Mono of {@link io.exchange.backend.assignment.tradingbot.model.order.Trade} info about closed position
     */
    Mono<Trade> closeOrder(String positionId);
}
