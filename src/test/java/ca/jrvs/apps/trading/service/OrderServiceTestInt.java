package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.*;
import ca.jrvs.apps.trading.model.dto.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTestInt {

    @InjectMocks
    OrderService orderService;
    @Captor
    ArgumentCaptor<SecurityOrder> captorSecurityOrder;
    //mock all dependencies
    @Mock
    private PositionDao positionDao;
    @Mock
    private AccountDao accountDao;
    @Mock
    private QuoteDao quoteDao;
    @Mock
    private TraderDao traderDao;
    @Mock
    private SecurityOrderDao securityOrderDao;
    private MarketOrderDto savedOrderDtoBuy;
    private MarketOrderDto savedOrderdtoSell;
    private Account savedAccount;
    private Trader savedTrader;
    private Quote quote;
    private SecurityOrder securityOrder;

    @Before
    public void init() {
        //setup some data
        savedOrderDtoBuy = new MarketOrderDto();
        savedOrderDtoBuy.setAccountId(1);
        savedOrderDtoBuy.setSize(2);
        savedOrderDtoBuy.setTicker("FB");

        savedOrderdtoSell = new MarketOrderDto();
        savedOrderdtoSell.setAccountId(1);
        savedOrderdtoSell.setSize(-3);
        savedOrderdtoSell.setTicker("FB");

        savedTrader = new Trader();
        savedTrader.setId(1);
        savedTrader.setCountry("Pak");
        savedTrader.setDob(LocalDate.parse("1993-06-22"));
        savedTrader.setEmail("saud@jrvs.ca");
        savedTrader.setFirstName("Saud");
        savedTrader.setLastName("Aslam");

        savedAccount = new Account();
        savedAccount.setId(1);
        savedAccount.setTraderId(savedTrader.getId());
        savedAccount.setAmount(10000.0);

        quote = new Quote();
        quote.setAskPrice(10.0);
        quote.setTicker("FB");
        quote.setAskSize((long) 100.00);
        quote.setBidSize((long) 123.00);
        quote.setBidPrice(122.0);
        quote.setId("FB");
        quote.setLastPrice(12.0);

    }

    @Test
    public void orderBuyHappy() throws JsonProcessingException {

        when(quoteDao.findById(anyString())).thenReturn(quote);
        when(accountDao.findById(anyInt())).thenReturn(savedAccount);

        List<MarketOrderDto> marketOrderDtos = Arrays.asList(savedOrderDtoBuy);

        when(accountDao.updateAccountbyID(anyDouble(), anyInt())).thenReturn(savedAccount);

        securityOrder = orderService.executeMarketOrder(marketOrderDtos);

        verify(securityOrderDao).save(captorSecurityOrder.capture());
        SecurityOrder captorOrder = captorSecurityOrder.getValue();
        assertEquals(SecurityOrder.orderStatus.FILLED, captorOrder.getStatus());

    }

    @Test
    public void orderBuySad() throws JsonProcessingException {
        savedAccount.setAmount(1.0);

        when(quoteDao.findById(anyString())).thenReturn(quote);
        when(accountDao.findById(anyInt())).thenReturn(savedAccount);

        List<MarketOrderDto> marketOrderDtos = Arrays.asList(savedOrderDtoBuy);
        securityOrder = orderService.executeMarketOrder(marketOrderDtos);

        verify(securityOrderDao).save(captorSecurityOrder.capture());
        SecurityOrder captorOrder = captorSecurityOrder.getValue();
        assertEquals(SecurityOrder.orderStatus.CANCELLED, captorOrder.getStatus());

    }

    @Test
    public void orderSellHappy() throws JsonProcessingException {

        when(quoteDao.findById(anyString())).thenReturn(quote);
        when(accountDao.findById(anyInt())).thenReturn(savedAccount);
        when(positionDao.getPosition(anyInt(), anyString())).thenReturn((long) 4);
        List<MarketOrderDto> marketOrderDtos = Arrays.asList(savedOrderdtoSell);
        securityOrder = orderService.executeMarketOrder(marketOrderDtos);

        verify(securityOrderDao).save(captorSecurityOrder.capture());
        SecurityOrder captorOrder = captorSecurityOrder.getValue();
        assertEquals(SecurityOrder.orderStatus.FILLED, captorOrder.getStatus());

    }

    @Test
    public void orderSellSad() throws JsonProcessingException {

        when(quoteDao.findById(anyString())).thenReturn(quote);
        when(accountDao.findById(anyInt())).thenReturn(savedAccount);
        when(positionDao.getPosition(anyInt(), anyString())).thenReturn((long) 2);
        List<MarketOrderDto> marketOrderDtos = Arrays.asList(savedOrderdtoSell);
        securityOrder = orderService.executeMarketOrder(marketOrderDtos);

        verify(securityOrderDao).save(captorSecurityOrder.capture());
        SecurityOrder captorOrder = captorSecurityOrder.getValue();
        assertEquals(SecurityOrder.orderStatus.CANCELLED, captorOrder.getStatus());

    }

}