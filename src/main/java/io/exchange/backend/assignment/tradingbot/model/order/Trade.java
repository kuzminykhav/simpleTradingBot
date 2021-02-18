package io.exchange.backend.assignment.tradingbot.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public final class Trade {
    private String id;
    private String positionId;
    private BigMoney profitAndLoss;
    private Product product;
    private BigMoney investingAmount;
    private BigMoney price;
    private Integer leverage;
    private TradeDirection direction;
    private TradeType type;
    private DateTime dateCreated;
}
