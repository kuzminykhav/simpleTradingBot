package io.exchange.backend.assignment.tradingbot.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import io.exchange.backend.assignment.tradingbot.model.quote.ConnectedEventBody;
import io.exchange.backend.assignment.tradingbot.model.quote.EventBody;
import io.exchange.backend.assignment.tradingbot.model.quote.FeedEvent;
import io.exchange.backend.assignment.tradingbot.model.quote.QuoteEventBody;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


class FeedEventBodyDeserializerTest {

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new SimpleModule() {{
        addDeserializer(EventBody.class, new FeedEventBodyDeserializer());
    }}).registerModule(new JodaModule());

    @Test
    public void givenJsonWithQuoteEventBody_whenDeserialize_thenSuccessQuoteEventBody() throws IOException {
        //given quote json
        String json = "{\"body\":{\"securityId\":\"sb26493\",\"currentPrice\":\"8511.6\",\"timeStamp\":1611933865904},\"t\":\"trading.quote\"}";
        //when deserialize
        FeedEvent event = objectMapper.readValue(json, FeedEvent.class);
        //then price correct
        assertEquals("8511.6", ((QuoteEventBody) event.getBody()).getCurrentPrice());
        assertEquals("trading.quote", event.getT());
    }

    @Test
    public void givenJsonWithConnectedEventBody_whenDeserialize_thenSuccessConnectedEventBody() throws IOException {
        //given connected json
        String json = "{\"body\":{\"userId\":\"bb0cda2b-a10e-4ed3-ad5a-0f82b4c152c4\",\"sessionId\":\"66bcf512-f85e-456e-85e6-646f5d8a379a\",\"time\":1611933864791},\"t\":\"connect.connected\"}";
        //when deserialize
        FeedEvent event = objectMapper.readValue(json, FeedEvent.class);
        //then session and user correct
        assertEquals("bb0cda2b-a10e-4ed3-ad5a-0f82b4c152c4", ((ConnectedEventBody) event.getBody()).getUserId());
        assertEquals("66bcf512-f85e-456e-85e6-646f5d8a379a", ((ConnectedEventBody) event.getBody()).getSessionId());
        assertEquals("connect.connected", event.getT());
    }

}