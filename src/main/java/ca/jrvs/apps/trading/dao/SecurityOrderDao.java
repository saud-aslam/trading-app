package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.dto.SecurityOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class SecurityOrderDao extends JdbcCrudDao<SecurityOrder, Integer> {

    @Autowired
    public SecurityOrderDao(DataSource dataSource) {
        super(dataSource, "security_order", "id", SecurityOrder.class, true);
    }

    public boolean deletebyAccountId(Integer accountId) {
        return super.deleteById("account_id", accountId);
    }
}
