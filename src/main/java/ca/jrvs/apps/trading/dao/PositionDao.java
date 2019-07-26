package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.dto.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.List;

public class PositionDao extends JdbcCrudDao<Position, Integer> {

    @Autowired
    public PositionDao(DataSource dataSource) {
        super(dataSource, null, "id", Position.class, false);
    }

    public List<Position> getPosition(Integer id) {
        String sql = "select * from position where account_id=?";
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Position.class), id);
    }
}
