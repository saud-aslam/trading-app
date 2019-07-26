package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.*;
import ca.jrvs.apps.trading.model.dto.Account;
import ca.jrvs.apps.trading.model.dto.Position;
import ca.jrvs.apps.trading.model.dto.Trader;
import ca.jrvs.apps.trading.model.view.TraderAccountView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class RegisterService {

    private TraderDao traderDao;
    private AccountDao accountDao;
    private PositionDao positionDao;
    private SecurityOrderDao securityOrderDao;

    @Autowired
    public RegisterService(TraderDao traderDao, AccountDao accountDao,
                           PositionDao positionDao, SecurityOrderDao securityOrderDao) {
        this.traderDao = traderDao;
        this.accountDao = accountDao;
        this.positionDao = positionDao;
        this.securityOrderDao = securityOrderDao;
    }

    /**
     * Create a new trader and initialize a new account with 0 amount.
     * - validate user input (all fields must be non empty)
     * - create a trader
     * - create an account
     * - create, setup, and return a new traderAccountView
     *
     * @param trader trader info
     * @return traderAccountView
     * @throws ca.jrvs.apps.trading.dao.ResourceNotFoundException if ticker is not found from IEX
     * @throws org.springframework.dao.DataAccessException        if unable to retrieve data
     * @throws IllegalArgumentException                           for invalid input
     */
    public TraderAccountView createTraderAndAccount(Trader trader) {
        if (isValid(trader)) {
            //saving trader in db
            traderDao.save(trader);
            //creating new account and saving in db
            Account account = new Account();
            account.setAmount(0.0);
            account.setTraderId(trader.getId());
            accountDao.save(account);
            TraderAccountView traderAccountView = new TraderAccountView();
            traderAccountView.setAccount(account);
            traderAccountView.setTrader(trader);
            return traderAccountView;
        } else
            throw new IllegalArgumentException("Validation of Trader failed");
    }

    /**
     * A trader can be deleted iff no open position and no cash balance.
     * - validate traderID
     * - get trader account by traderId and check account balance
     * - get positions by accountId and check positions
     * - delete all securityOrders, account, trader (in this order)
     *
     * @param traderId
     * @throws ca.jrvs.apps.trading.dao.ResourceNotFoundException if ticker is not found from IEX
     * @throws org.springframework.dao.DataAccessException        if unable to retrieve data
     * @throws IllegalArgumentException                           for invalid input
     */
    public void deleteTraderById(Integer traderId) {
        if (traderId == null) {
            throw new IllegalArgumentException("TraderId is not valid");
        }

        if (!(traderDao.existsById(traderId))) {
            throw new ResourceNotFoundException("Trader does not exists");
        }
        Account account = accountDao.findByTraderId(traderId);
        if (account.getAmount() != 0.0) {
            throw new IllegalArgumentException("Account has some balance, can not be deleted");
        }

        List<Position> positions = positionDao.getPosition(account.getId());
        if (positions.size() != 0) {
            throw new IllegalArgumentException("Account has some positions, can not be deleted");
        }

        securityOrderDao.deletebyAccountId(account.getId());
        accountDao.deleteById(account.getId());
        traderDao.deleteById(traderId);
    }

    public boolean isValid(Trader trader) {
        Field[] fields = trader.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                if (field.get(trader) == null && field.getName() != "id") {
                    throw new IllegalArgumentException("Trader has a null value at:" + field.getName());
                }
            } catch (IllegalAccessException e) {
                e.getMessage();
            }
        }
        return true;
    }

}


