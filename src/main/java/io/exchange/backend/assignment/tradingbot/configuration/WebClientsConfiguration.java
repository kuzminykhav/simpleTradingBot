package io.exchange.backend.assignment.tradingbot.configuration;

import io.exchange.backend.assignment.tradingbot.exception.TradeException;
import io.exchange.backend.assignment.tradingbot.model.order.OrderError;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class WebClientsConfiguration {

    @Bean
    public WebSocketClient webSocketClient() {
        return new ReactorNettyWebSocketClient();
    }

    @Bean
    public WebClient webClient(TradeProperties tradeProperties) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .wiretap(true)
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));

        return WebClient.builder()
                .baseUrl(tradeProperties.getQuoteServerUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, tradeProperties.getStockExchangeAuthToken())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter(ExchangeFilterFunctions.statusError(status -> status.is5xxServerError() || status.is4xxClientError(), clientResponse ->
                        {
                            clientResponse.bodyToMono(OrderError.class).doOnNext(err ->
                                    log.error("Order error message {} code {}", err.getMessage(), err.getErrorCode())
                            ).subscribe();
                            return new TradeException("Order client exception");
                        }
                ))
                .build();
    }
}
