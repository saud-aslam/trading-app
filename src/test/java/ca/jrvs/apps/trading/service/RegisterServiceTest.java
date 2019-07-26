package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.PositionDao;
import ca.jrvs.apps.trading.dao.SecurityOrderDao;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.dto.Trader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.time.LocalDate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ca.jrvs.apps.trading.TestConfig.class,
        loader = AnnotationConfigContextLoader.class)

public class RegisterServiceTest {
    private Trader trader;

    @Autowired
    private RegisterService registerService;
    private TraderDao traderDao;
    private AccountDao accountDao;
    private PositionDao positionDao;
    private SecurityOrderDao securityOrderDao;

    @Before
    public void setup() {
        this.trader = new Trader();
        trader.setCountry("Pakistan");
        trader.setDob(LocalDate.parse("1993-06-22"));
        trader.setEmail("saud@gmail");
        trader.setFirstName("Saud");
        trader.setLastName("Asslam");
    }

    @Test
    public void createTraderAndAccount() {
        registerService.createTraderAndAccount(trader);
    }

}