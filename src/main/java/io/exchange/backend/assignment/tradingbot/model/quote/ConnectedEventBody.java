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
public class ConnectedEventBody implements EventBody {
    private String userId;
    private String sessionId;
    private DateTime time;
}
