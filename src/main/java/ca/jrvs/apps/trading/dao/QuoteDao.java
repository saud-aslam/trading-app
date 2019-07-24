package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.dto.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class QuoteDao extends JdbcCrudDao<Quote, String> {



    @Autowired
    public QuoteDao(DataSource dataSource) {
        super(dataSource, "quote", "ticker", Quote.class, false);
    }

    public List<Quote> findAll() {
        String sql = "SELECT * FROM  quote";

        List<Quote> quotes = jdbcTemplate.query(sql,
                new BeanPropertyRowMapper(Quote.class));

        return quotes;
    }

}




