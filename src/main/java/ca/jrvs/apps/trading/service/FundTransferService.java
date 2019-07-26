package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.AccountDao;
import ca.jrvs.apps.trading.dao.ResourceNotFoundException;
import ca.jrvs.apps.trading.dao.TraderDao;
import ca.jrvs.apps.trading.model.dto.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FundTransferService {

    private AccountDao accountDao;
    private TraderDao traderDao;

    @Autowired
    public FundTransferService(AccountDao accountDao, TraderDao traderDao) {
        this.accountDao = accountDao;
        this.traderDao = traderDao;
    }

    /**
     * Deposit a fund to the account which is associated with the traderId
     * - validate user input
     * - account = accountDao.findByTraderId
     * - accountDao.updateAmountById
     *
     * @param traderId trader id
     * @param fund     found amount (can't be 0)
     * @return updated Account object
     * @throws ca.jrvs.apps.trading.dao.ResourceNotFoundException if ticker is not found from IEX
     * @throws org.springframework.dao.DataAccessException        if unable to retrieve data
     * @throws IllegalArgumentException                           for invalid input
     */
    public Account deposit(Integer traderId, Double fund) {

        if (traderId == null) {
            throw new IllegalArgumentException("TraderId is not valid");
        }

        if (fund <= 0.0) {
            throw new IllegalArgumentException("Fund amount has to be greater than zero");
        }

        if (!(traderDao.existsById(traderId))) {
            throw new ResourceNotFoundException("Trader does not exists");
        }
        Account account = accountDao.findByTraderId(traderId);
        try {
            return accountDao.updateAccountbyID(fund, account.getId());
        } catch (IncorrectResultSizeDataAccessException e) {
            e.getLocalizedMessage();
        }
        return null;
    }

    /**
     * Withdraw a fund from the account which is associated with the traderId
     * <p>
     * - validate user input
     * - account = accountDao.findByTraderId
     * - accountDao.updateAmountById
     *
     * @param traderId trader ID
     * @param fund     amount can't be 0
     * @return updated Account object
     * @throws ca.jrvs.apps.trading.dao.ResourceNotFoundException if ticker is not found from IEX
     * @throws org.springframework.dao.DataAccessException        if unable to retrieve data
     * @throws IllegalArgumentException                           for invalid input
     */
    public Account withdraw(Integer traderId, Double fund) {
        if (traderId == null) {
            throw new IllegalArgumentException("TraderId is not valid");
        }

        if (fund <= 0.0) {
            throw new IllegalArgumentException("Fund amount has to be greater than zero");
        }

        if (!(traderDao.existsById(traderId))) {
            throw new ResourceNotFoundException("Trader does not exists");
        }
        Account account = accountDao.findByTraderId(traderId);
        if (account.getAmount() < 0 || account.getAmount() < fund)
            throw new IllegalArgumentException("You don't have enough amount available to proceed with withdrawing");
        try {
            return accountDao.updateAccountbyID((account.getAmount() - fund), account.getId());
        } catch (IncorrectResultSizeDataAccessException e) {
            e.getLocalizedMessage();
        }
        return null;
    }
}
