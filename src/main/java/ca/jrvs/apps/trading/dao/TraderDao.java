package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.dto.Trader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class TraderDao extends JdbcCrudDao<Trader, Integer> {
    @Autowired
    public TraderDao(DataSource dataSource) {
        super(dataSource, "trader", "id", Trader.class, true);
    }

}
