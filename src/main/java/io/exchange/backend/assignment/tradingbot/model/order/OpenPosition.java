package io.exchange.backend.assignment.tradingbot.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class OpenPosition {
    private final String productId;
    private final BigMoney investingAmount;
    private final Integer leverage;
    private final TradeDirection direction;
    private final Source source;
}
