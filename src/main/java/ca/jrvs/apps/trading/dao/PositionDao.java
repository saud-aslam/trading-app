package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.dto.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class PositionDao extends JdbcCrudDao<Position, Integer> {

    @Autowired
    public PositionDao(DataSource dataSource) {
        super(dataSource, null, "id", Position.class, false);
    }

    public List<Position> getPosition(Integer id) {
        String sql = "select * from position where account_id=?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Position.class), id);
    }

    public Long getPosition(Integer id, String ticker) {
        String sql = "select position from position where account_id=? and ticker=?";
        return jdbcTemplate.queryForObject(sql, Long.class, id, ticker);
    }
}
