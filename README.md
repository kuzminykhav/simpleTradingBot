# Requirements
Very basic Trading Bot that tracks the price of a certain product and will execute a pre-defined trade in the
said product when it reaches a given price. After the product has been bought the Trading Bot should keep on tracking the prices and execute a
sell order when a certain price is hit. In the input, there should be an "upper limit" price and a "lower limit" price.

### Solution
Trade engine built based on Spring StateMachine. Diagram:

NEW-->(QUOTE event)-->TRADE_BUY(BuyGuard+BuyAction)-->(HOLD event)-->HOLDING->(QUOTE event)-->TRADE_SELL(SellGuard+SellAction)-
 ||                                           |                        |                                               |      |
 |<-------------------------------------------|                        <------------------------------------------------      |                    
 |                                                                                                                            |
 <-----------------------------------------------------------------------------------------------------------------------------

### Guides

java -jar tradingBot-0.0.1-SNAPSHOT.jar --exchange.bot.buyPrice=14925 --exchange.bot.takeProfitPrice=12930 --exchange.bot.stopLossPrice=12820 --exchange.bot.productId=sb26493




