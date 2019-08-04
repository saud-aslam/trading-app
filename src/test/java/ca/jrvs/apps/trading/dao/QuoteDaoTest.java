/**package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.dto.Quote;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ca.jrvs.apps.trading.TestConfig.class,
        loader = AnnotationConfigContextLoader.class)
public class QuoteDaoTest {

    @Autowired
    private QuoteDao quoteDao;

    @Test
    public void saveAnddeletebyId() {

        assertFalse(quoteDao.deleteById("HAHA"));

        Quote quote = new Quote();
        quote.setAskPrice(100.0);
        quote.setTicker("Fake");
        quote.setAskSize((long) 100.00);
        quote.setBidSize((long) 123.00);
        quote.setBidPrice(122.0);
        quote.setId("Fake");
        quote.setLastPrice(12.0);
        assertEquals(quote, quoteDao.save(quote));
        assertTrue(quoteDao.deleteById("Fake"));

    }

    @Test
    public void findbyId() {
        Quote quote = quoteDao.findById("FB");
        Double askPrice = (quoteDao.findById("FB").getAskPrice());
        assertEquals("FB", quote.getTicker());
        System.out.println(askPrice);

    }

    @Test
    public void findbyIdForUpdate() {
        Quote quote = quoteDao.findByIdForUpdate("FB");
        assertEquals("FB", quote.getTicker());
    }

    @Test
    public void existbyId() {

        assertTrue(quoteDao.existsById("FB"));
        assertFalse(quoteDao.existsById("HAHAHA"));
    }

    @Test
    public void returnAlltickers() {

        System.out.println(quoteDao.returnAllTickers());
    }


}

 
*/
