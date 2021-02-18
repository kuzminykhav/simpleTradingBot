package io.exchange.backend.assignment.tradingbot.service.order;

import io.exchange.backend.assignment.tradingbot.model.order.BigMoney;
import io.exchange.backend.assignment.tradingbot.model.order.OpenPosition;
import io.exchange.backend.assignment.tradingbot.model.order.Source;
import io.exchange.backend.assignment.tradingbot.model.order.Trade;
import io.exchange.backend.assignment.tradingbot.model.order.TradeDirection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import static io.exchange.backend.assignment.tradingbot.model.order.TradeSourceType.SHARED_TRADE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    private OrderService orderService;

    private final String testPositionId = "testPositionId";
    private final BigDecimal inputAmount = BigDecimal.TEN;
    private final String exchangeMockServerOpenPath = "serverOpenPath";
    private final String exchangeMockServerClosePath = "serverClosePath";
    private final String exchangeMockServerVersion = "21";


    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient webClient;
    @Mock
    private WebClient.RequestHeadersSpec headersSpecMock;
    @Mock
    private WebClient.ResponseSpec responseSpecMock;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriMock;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriMock;


    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(webClient, exchangeMockServerOpenPath, exchangeMockServerClosePath, exchangeMockServerVersion);
    }

    @Test
    void givenProductIdAmount_whenOpenOrder_thenTradeDetails() {
        //given
        DecimalFormat amountFormat = new DecimalFormat();
        amountFormat.setMaximumFractionDigits(2);
        amountFormat.setMinimumFractionDigits(0);
        amountFormat.setGroupingUsed(false);
        String inputProductId = "testProductId";
        OpenPosition openPosition = OpenPosition.builder()
                .leverage(2)
                .productId(inputProductId)
                .direction(TradeDirection.BUY)
                .source(Source.builder()
                        .sourceType(SHARED_TRADE)
                        .build())
                .investingAmount(BigMoney.builder()
                        .amount(amountFormat.format(inputAmount))
                        .currency("BUX")
                        .decimals(2)
                        .build())
                .build();

        when(webClient.post()).thenReturn(requestBodyUriMock);
        when(requestBodyUriMock.uri(exchangeMockServerOpenPath, exchangeMockServerVersion, inputProductId)).thenReturn(requestBodyUriMock);
        when(requestBodyUriMock.accept(MediaType.APPLICATION_JSON)).thenReturn(requestBodyUriMock);
        when(requestBodyUriMock.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodyUriMock);
        when(requestBodyUriMock.header(HttpHeaders.ACCEPT_LANGUAGE, "nl-NL,en;q=0.8")).thenReturn(requestBodyUriMock);
        //when(requestBodyUriMock.headers(any())).thenReturn(requestBodyUriMock);
        when(requestBodyUriMock.body(any())).thenReturn(headersSpecMock);
        when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(Trade.class))
                .thenReturn(Mono.just(Trade.builder().positionId(testPositionId).build()));
        //when
        StepVerifier
                .create(orderService.openOrder(inputProductId, inputAmount))
                .consumeNextWith(s -> {
                    //then
                    assertEquals(testPositionId, s.getPositionId());
                }).verifyComplete();
    }

    @Test
    void givenPositionId_whenCloseOrder_thenTradeDetails() {
        //given
        when(webClient.delete()).thenReturn(requestHeadersUriMock);
        when(requestHeadersUriMock.uri(exchangeMockServerClosePath, exchangeMockServerVersion, testPositionId)).thenReturn(headersSpecMock);
        when(headersSpecMock.accept(MediaType.APPLICATION_JSON)).thenReturn(headersSpecMock);
        when(headersSpecMock.header(HttpHeaders.ACCEPT_LANGUAGE, "nl-NL,en;q=0.8")).thenReturn(headersSpecMock);
        when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(Trade.class))
                .thenReturn(Mono.just(Trade.builder().positionId(testPositionId).build()));
        //when
        StepVerifier
                .create(orderService.closeOrder(testPositionId))
                .consumeNextWith(s -> {
                    //then
                    assertEquals(testPositionId, s.getPositionId());
                }).verifyComplete();
    }

}