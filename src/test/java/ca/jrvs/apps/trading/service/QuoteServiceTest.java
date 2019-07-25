

package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.model.dto.IexQuote;
import ca.jrvs.apps.trading.model.dto.Quote;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ca.jrvs.apps.trading.TestConfig.class,
        loader = AnnotationConfigContextLoader.class)
public class QuoteServiceTest {

    @Autowired
    private QuoteService quoteService;
    private MarketDataDao marketDataDao;

    @Test
    public void buildQuoteFromIexQuote() {

        IexQuote iexQuote = new IexQuote();
        iexQuote.setIexAskPrice(19.00);
        iexQuote.setLatestPrice(12.00);
        iexQuote.setSymbol("Twt");
        iexQuote.setIexBidPrice(120.00);
        iexQuote.setIexBidSize((long) 166.0);
        iexQuote.setIexAskSize((long) 122.0);

        Quote testQuote = new Quote();
        testQuote.setTicker("Twt");
        testQuote.setLastPrice(12.00);
        testQuote.setId("Twt");
        testQuote.setBidPrice(120.00);
        testQuote.setBidSize((long) 166.0);
        testQuote.setAskSize((long) 122.0);
        testQuote.setAskPrice(19.00);

        Quote expectedQuote = quoteService.buildQuoteFromIexQuote(iexQuote);
        assertEquals(testQuote, expectedQuote);
        assertEquals(testQuote.getAskPrice(), expectedQuote.getAskPrice());
        assertEquals(testQuote.getAskSize(), expectedQuote.getAskSize());
        assertEquals(testQuote.getId(), expectedQuote.getId());
        assertEquals(testQuote.getTicker(), expectedQuote.getTicker());
        assertEquals(testQuote.getBidPrice(), expectedQuote.getBidPrice());
        assertEquals(testQuote.getBidSize(), expectedQuote.getBidSize());

    }

    @Test
    public void initQuotes() {
        quoteService.initQuote("mcd");
    }

    @Test
    public void marketDataUpdate() {
        quoteService.updateMarketData();
    }

}


