package io.exchange.backend.assignment.tradingbot.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.exchange.backend.assignment.tradingbot.model.quote.ConnectedEventBody;
import io.exchange.backend.assignment.tradingbot.model.quote.EventBody;
import io.exchange.backend.assignment.tradingbot.model.quote.QuoteEventBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * Deserializer for {@link io.exchange.backend.assignment.tradingbot.model.quote.EventBody}
 * Supports only QuoteEvent and ConnectionEvent
 */
@Slf4j
@JsonComponent
public class FeedEventBodyDeserializer extends JsonDeserializer<EventBody> {

    @Override
    public EventBody deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException {
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();
        ObjectNode root = mapper.readTree(jsonParser);
        if (root.has("currentPrice")) {
            return mapper.readValue(root.toString(), QuoteEventBody.class);
        } else {
            return mapper.readValue(root.toString(), ConnectedEventBody.class);
        }
    }
}
