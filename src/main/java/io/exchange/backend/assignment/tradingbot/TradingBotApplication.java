package io.exchange.backend.assignment.tradingbot;


import io.exchange.backend.assignment.tradingbot.service.trade.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.Order;

@Order(-1)
@SpringBootApplication
@Slf4j
public class TradingBotApplication implements CommandLineRunner {

    private final TradeService tradeService;

    public TradingBotApplication(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(TradingBotApplication.class);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        tradeService.makeTrade().subscribe();
    }
}
