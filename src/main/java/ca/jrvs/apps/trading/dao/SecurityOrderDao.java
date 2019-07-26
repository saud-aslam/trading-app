package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.dto.SecurityOrder;
import ca.jrvs.apps.trading.model.dto.Trader;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

public class SecurityOrderDao extends JdbcCrudDao<SecurityOrder, Integer> {

    @Autowired
    public SecurityOrderDao(DataSource dataSource) {
        super(dataSource, "security_order", "id", Trader.class, true);
    }

    public boolean deletebyAccountId(Integer accountId) {
        return super.deleteById("account_id", accountId);
    }
}
