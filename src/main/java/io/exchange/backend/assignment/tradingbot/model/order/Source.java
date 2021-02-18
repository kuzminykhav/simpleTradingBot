package io.exchange.backend.assignment.tradingbot.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public final class Source {
    private TradeSourceType sourceType;
    //private final String sourceId;
}
