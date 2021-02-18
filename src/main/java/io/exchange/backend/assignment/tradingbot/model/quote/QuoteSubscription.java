package io.exchange.backend.assignment.tradingbot.model.quote;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class QuoteSubscription {
    private final List<String> subscribeTo;
    private final List<String> unsubscribeFrom;
}
