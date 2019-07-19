package ca.jrvs.apps.trading.dao;

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
public class QuoteDaoTest {

    @Autowired
    private QuoteDao quoteDao;

    @Test
    public void save() {
        Quote quote = new Quote();
        quote.setAskPrice(10.0);
        quote.setTicker("FB");
        System.out.println(quote);
        assertEquals(quote, quoteDao.save(quote));

    }
}