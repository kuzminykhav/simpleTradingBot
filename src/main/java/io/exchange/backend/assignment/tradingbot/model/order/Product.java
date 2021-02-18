package io.exchange.backend.assignment.tradingbot.model.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public final class Product {
    private String securityId;
    private String symbol;
    private String displayName;
    private BigMoney closingPrice;
    private BigMoney currentPrice;
}
