package io.exchange.backend.assignment.tradingbot.service.feed;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.exchange.backend.assignment.tradingbot.configuration.TradeProperties;
import io.exchange.backend.assignment.tradingbot.model.quote.FeedEvent;
import io.exchange.backend.assignment.tradingbot.model.quote.QuoteSubscription;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.net.URI;
import java.util.Collections;


/**
 * Implementation for EventFeedService interface. Read events from BUX server web socket
 */

@RequiredArgsConstructor
@Slf4j
@Service
public class ExchangeEventFeedServiceImpl implements EventFeedService {

    private final ObjectMapper objectMapper;
    private final TradeProperties tradeProperties;
    private final WebSocketClient webSocketClient;

    /**
     * @param productId id of product for subscription
     * @return Flux of Events from websocket
     */
    @Override
    public Flux<FeedEvent> getEvents(String productId) {

        Sinks.Many<FeedEvent> quotes = Sinks.many().multicast().directBestEffort();

        //subscription request
        QuoteSubscription quoteSubscription = QuoteSubscription
                .builder()
                .subscribeTo(Collections.singletonList("trading.product." + tradeProperties.getProductId()))
                .build();
        String subscribtionRequest = null;
        try {
            subscribtionRequest = objectMapper.writeValueAsString(quoteSubscription);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        String finalSubscribtionRequest = subscribtionRequest;


        webSocketClient.execute(URI.create(tradeProperties.getQuoteServerUrl()),
                authHeaders(tradeProperties.getStockExchangeAuthToken()),
                session -> session
                        //duplex handler
                        .send(
                                session
                                        .receive()
                                        .map(WebSocketMessage::getPayloadAsText)
                                        //filter only for events
                                        .filter(s -> s.contains("body"))
                                        .map(s -> {
                                            try {
                                                return objectMapper.readValue(s, FeedEvent.class);
                                            } catch (JsonProcessingException e) {
                                                log.warn("Unexpected event! " + s);
                                                return new FeedEvent();
                                            }
                                        })
                                        //publish quote to sink
                                        .doOnNext(quotes::tryEmitNext)
                                        .doOnError(e ->
                                                log.error("Event feed error {}", e.getMessage())
                                        )
                                        .doOnTerminate(() -> {
                                            log.error("Event feed connection terminated");
                                        })
                                        .retry(5)
                                        //send subscrintion event only if connected
                                        .filter(s -> s.getT().equals("connect.connected"))
                                        .map(value -> session.textMessage(finalSubscribtionRequest))
                        )
                        .doOnError(e ->
                                log.error("Event feed error {}", e.getMessage()))
                        .then()
        ).subscribe();


        return quotes.asFlux();
    }

    private HttpHeaders authHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, token);
        return headers;
    }
}
