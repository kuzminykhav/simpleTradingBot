package io.exchange.backend.assignment.tradingbot.model.quote;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.DateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QuoteEventBody implements EventBody {
    private String securityId;
    private String currentPrice;
    private DateTime timeStamp;
}
