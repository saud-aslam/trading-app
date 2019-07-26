package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.dto.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class AccountDao extends JdbcCrudDao<Account, Integer> {

    @Autowired
    public AccountDao(DataSource dataSource) {
        super(dataSource, "account", "id", Account.class, true);
    }

    public Account findByTraderId(Integer traderId) {
        return super.findById("trader_id", traderId, false, Account.class);
    }

    public Account findByTraderIdForUpdate(Integer traderId) {
        return super.findById("trader_id", traderId, true, Account.class);
    }

    public Account updateAccountbyID(Double fund, Integer accoundId) {
        String sql = "update account set amount = ? where id = ?";
        if ((jdbcTemplate.update(sql, fund, accoundId) != 1)) {
            throw new IncorrectResultSizeDataAccessException("Expected size is 1", jdbcTemplate.update(sql, fund, accoundId));
        }
        return findById(accoundId);
    }
}
