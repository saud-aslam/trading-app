package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.dto.Account;
import ca.jrvs.apps.trading.model.dto.Position;
import ca.jrvs.apps.trading.model.dto.Quote;
import ca.jrvs.apps.trading.model.dto.Trader;
import ca.jrvs.apps.trading.model.view.PortfolioView;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DashboardServiceTestInt {

    //inject mocked dependencies to the testing class via constructor
    @InjectMocks
    DashboardService dashboardService;
    //mock all dependencies
    @Mock
    private PositionDao positionDao;
    @Mock
    private AccountDao accountDao;
    @Mock
    private QuoteDao quoteDao;
    @Mock
    private TraderDao traderDao;
    private Trader savedTrader;
    private Account savedAccount;

    @Before
    public void init() {
        //setup some data
        savedTrader = new Trader();
        savedTrader.setId(1);
        savedTrader.setCountry("US");
        savedTrader.setDob(LocalDate.parse("1993-06-22"));
        savedTrader.setEmail("edward@jrvs.ca");
        savedTrader.setFirstName("edward");
        savedTrader.setLastName("wang");

        savedAccount = new Account();
        savedAccount.setId(1);
        savedAccount.setTraderId(savedTrader.getId());
        savedAccount.setAmount(100.0);

    }

    @Test
    public void buildProfileViewByTraderId() throws JsonProcessingException {
        when(accountDao.findByTraderId(anyInt())).thenReturn(savedAccount);

        Position p1 = new Position();
        p1.setId(savedAccount.getId());
        p1.setPosition(10);
        p1.setTicker("apple");

        Position p2 = new Position();
        p2.setId(savedAccount.getId());
        p2.setPosition(1);
        p2.setTicker("amazon");

        List<Position> positions = Arrays.asList(p1, p2);

        when(positionDao.getPosition(anyInt())).thenReturn(positions);
        Quote appleQuote = new Quote();
        appleQuote.setTicker(p1.getTicker());
        appleQuote.setLastPrice(101.0);

        when(quoteDao.findById(p1.getTicker())).thenReturn(appleQuote);

        Quote amazonQuote = new Quote();
        amazonQuote.setTicker(p2.getTicker());
        amazonQuote.setLastPrice(990.0);

        when(quoteDao.findById(p2.getTicker())).thenReturn(amazonQuote);

        when(traderDao.existsById(anyInt())).thenReturn(Boolean.TRUE);

        PortfolioView view = dashboardService.getProfileViewByTraderId(savedTrader.getId());
        assertNotNull(view.getSecurityRows().size());
        assertEquals(2, view.getSecurityRows().size());
    }

}