package io.exchange.backend.assignment.tradingbot.service.order;

import io.exchange.backend.assignment.tradingbot.model.order.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import static io.exchange.backend.assignment.tradingbot.model.order.TradeSourceType.SHARED_TRADE;

/**
 * Implementation of OrderService
 */

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final WebClient webClient;
    private final String exchangeServerOpenPath;
    private final String exchangeServerClosePath;
    private final String exchangeServerVersion;

    public OrderServiceImpl(WebClient webClient,
                            @Value("${exchange.bot.trade.server.open.path}") String exchangeServerOpenPath,
                            @Value("${exchange.bot.trade.server.close.path}") String exchangeServerClosePath,
                            @Value("${exchange.bot.trade.server.version}") String exchangeServerVersion) {
        this.webClient = webClient;
        this.exchangeServerOpenPath = exchangeServerOpenPath;
        this.exchangeServerClosePath = exchangeServerClosePath;
        this.exchangeServerVersion = exchangeServerVersion;
    }

    /**
     * Calling BUX server for opening position
     *
     * @param productId id of product
     * @param amount
     * @return
     */
    @Override
    public Mono<Trade> openOrder(String productId, BigDecimal amount) {
        log.info("openOrder start");
        amount = amount.setScale(2, RoundingMode.DOWN);
        DecimalFormat amountFormat = new DecimalFormat();
        amountFormat.setMaximumFractionDigits(2);
        amountFormat.setMinimumFractionDigits(0);
        amountFormat.setGroupingUsed(false);

        OpenPosition openPosition = OpenPosition.builder()
                .leverage(2)
                .productId(productId)
                .direction(TradeDirection.BUY)
                .source(Source.builder()
                        .sourceType(SHARED_TRADE)
                        .build())
                .investingAmount(BigMoney.builder()
                        .amount(amountFormat.format(amount))
                        .currency("BUX")
                        .decimals(2)
                        .build())
                .build();

        return webClient
                .post()
                .uri(exchangeServerOpenPath, exchangeServerVersion, productId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "nl-NL,en;q=0.8")
                .body(BodyInserters.fromValue(openPosition))
                .retrieve()
                .bodyToMono(Trade.class);
    }

    /**
     * Calling BUX server for clossing position
     *
     * @param positionId ID of position for closing
     * @return
     */
    @Override
    public Mono<Trade> closeOrder(String positionId) {
        return webClient
                .delete()
                .uri(exchangeServerClosePath, exchangeServerVersion, positionId)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "nl-NL,en;q=0.8")
                .retrieve()
                .bodyToMono(Trade.class);

    }
}
