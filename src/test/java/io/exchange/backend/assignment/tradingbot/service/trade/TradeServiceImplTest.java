package io.exchange.backend.assignment.tradingbot.service.trade;

import io.exchange.backend.assignment.tradingbot.configuration.TradeProperties;
import io.exchange.backend.assignment.tradingbot.model.quote.ConnectedEventBody;
import io.exchange.backend.assignment.tradingbot.model.quote.FeedEvent;
import io.exchange.backend.assignment.tradingbot.model.quote.QuoteEventBody;
import io.exchange.backend.assignment.tradingbot.service.feed.EventFeedService;
import io.exchange.backend.assignment.tradingbot.statemachine.event.TradeEvents;
import io.exchange.backend.assignment.tradingbot.statemachine.state.TradeState;
import org.joda.time.DateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.statemachine.StateMachine;
import reactor.core.publisher.Flux;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TradeServiceImplTest {

    private TradeService tradeService;

    private final String productId = "testProductId";

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private StateMachine<TradeState, TradeEvents> stateMachine;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private EventFeedService eventFeedService;
    @Mock
    private TradeProperties tradeProperties;

    @BeforeEach
    void setUp() {
        tradeService = new TradeServiceImpl(stateMachine, eventFeedService, tradeProperties);
    }

    @Test
    void given2QuoteEvents_whenMakeTrade_thenSubscribeOnQuotes() {
        //given
        Flux<FeedEvent> events = Flux.just(FeedEvent
                        .builder().body(QuoteEventBody
                                .builder()
                                .currentPrice("10.00")
                                .securityId(productId)
                                .build()).build(),
                FeedEvent
                        .builder()
                        .body(QuoteEventBody
                                .builder()
                                .currentPrice("11.00")
                                .securityId(productId)
                                .build())
                        .build(),
                FeedEvent
                        .builder()
                        .body(ConnectedEventBody
                                .builder().time(new DateTime("2004-12-13T21:39:45.618-08:00"))
                                .build())
                        .build());
        when(tradeProperties.getProductId()).thenReturn(productId);
        when(eventFeedService.getEvents(productId)).thenReturn(events);
        //when
        tradeService.makeTrade().subscribe();
        //then
        verify(stateMachine.getExtendedState().getVariables(), times(6)).put(any(), any());
        verify(stateMachine, times(2)).sendEvent(TradeEvents.QUOTE);

    }


}