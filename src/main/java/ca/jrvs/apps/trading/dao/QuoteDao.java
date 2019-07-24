package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.dto.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class QuoteDao extends JdbcCrudDao<Quote, String> {



    @Autowired
    public QuoteDao(DataSource dataSource) {
        super(dataSource, "quote", "ticker", Quote.class, false);
    }



}




