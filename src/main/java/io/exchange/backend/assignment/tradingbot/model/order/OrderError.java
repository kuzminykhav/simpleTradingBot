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
public class OrderError {
    private String message;
    private String developerMessage;
    private String errorCode;

}
