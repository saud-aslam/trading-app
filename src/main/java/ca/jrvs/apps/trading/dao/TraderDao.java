package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.dto.Trader;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

public class TraderDao extends JdbcCrudDao<Trader, Integer> {
    @Autowired
    public TraderDao(DataSource dataSource) {
        super(dataSource, "trader", "id", Trader.class, true);
    }

    public boolean existsById(Integer traderId) {
        return super.existsById("trader_id", traderId);

    }

}
