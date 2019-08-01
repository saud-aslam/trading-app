package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.*;
import ca.jrvs.apps.trading.model.dto.*;
import ca.jrvs.apps.trading.model.view.PortfolioView;
import ca.jrvs.apps.trading.model.view.TraderAccountView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DashboardService {
    private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);

    private TraderDao traderDao;
    private PositionDao positionDao;
    private AccountDao accountDao;
    private QuoteDao quoteDao;

    @Autowired
    public DashboardService(TraderDao traderDao, PositionDao positionDao, AccountDao accountDao,
                            QuoteDao quoteDao) {
        this.traderDao = traderDao;
        this.positionDao = positionDao;
        this.accountDao = accountDao;
        this.quoteDao = quoteDao;
    }

    /**
     * Create and return a traderAccountView by trader ID
     * - get trader account by id
     * - get trader info by id
     * - create and return a traderAccountView
     *
     * @param traderId trader ID
     * @return traderAccountView
     * @throws ca.jrvs.apps.trading.dao.ResourceNotFoundException if ticker is not found from IEX
     * @throws org.springframework.dao.DataAccessException        if unable to retrieve data
     * @throws IllegalArgumentException                           for invalid input
     */
    public TraderAccountView getTraderAccount(Integer traderId) {
        if (traderId < 0 || !traderDao.existsById(traderId))
            throw new IllegalArgumentException("TraderID: " + traderId + " not valid");
        Trader trader = null;
        Account account = null;
        try {
            trader = traderDao.findById(traderId);
            account = accountDao.findByTraderId(traderId);
        } catch (ResourceNotFoundException e) {
            e.getMessage();
        }

        TraderAccountView traderAccountView = new TraderAccountView();
        traderAccountView.setTrader(trader);
        traderAccountView.setAccount(account);

        return traderAccountView;

    }

    /**
     * Create and return portfolioView by trader ID
     * - get account by trader id
     * - get positions by account id
     * - create and return a portfolioView
     *
     * @param traderId
     * @return portfolioView
     * @throws ca.jrvs.apps.trading.dao.ResourceNotFoundException if ticker is not found from IEX
     * @throws org.springframework.dao.DataAccessException        if unable to retrieve data
     * @throws IllegalArgumentException                           for invalid input
     */
    public PortfolioView getProfileViewByTraderId(Integer traderId) {

        if (traderId < 0 || !traderDao.existsById(traderId))
            throw new IllegalArgumentException("TraderID: " + traderId + " not valid");
        SecurityRows securityRows = new SecurityRows();
        Account account = accountDao.findByTraderId(traderId);
        List<Position> positionList = positionDao.getPosition(account.getId());
        List<SecurityRows> securityRowss = new ArrayList<>();
      
        for (Position position : positionList) {
            Quote quote = quoteDao.findById(position.getTicker());
            securityRows.setQuote(quote);
            securityRows.setPosition(position);
            securityRows.setTicker(position.getTicker());
            securityRowss.add(securityRows);
        }

        PortfolioView portfolioView = new PortfolioView();
        portfolioView.setSecurityRows(securityRowss);

        return portfolioView;

    }
}
