package io.exchange.backend.assignment.tradingbot.configuration;

import io.exchange.backend.assignment.tradingbot.configuration.validate.ConsistentPriceLimits;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

/**
 * Initial properties of trade
 */

@Component
@ConfigurationProperties(prefix = "exchange.bot")
@Validated
@ConsistentPriceLimits(message = "buy price should be in between sell price and upper limit sell price")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TradeProperties {
    private BigDecimal buyPrice;

    private BigDecimal takeProfitPrice;

    private BigDecimal stopLossPrice;

    private String productId;

    private String stockExchangeAuthToken;

    private String stockExchangeServerUrl;

    @Value("${exchange.bot.quote.server}")
    private String quoteServerUrl;

}
