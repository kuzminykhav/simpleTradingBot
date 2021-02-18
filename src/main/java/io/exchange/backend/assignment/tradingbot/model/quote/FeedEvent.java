package io.exchange.backend.assignment.tradingbot.model.quote;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FeedEvent {
    private String t;
    private EventBody body;

}
