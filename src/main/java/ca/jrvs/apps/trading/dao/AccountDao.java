package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.dto.Account;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

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
}
