package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.dto.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;

public abstract class JdbcCrudDao<E extends Entity, ID> implements CrudRepository<E, ID> {

    private static final Logger logger = LoggerFactory.getLogger(JdbcCrudDao.class);

    public String TableName;
    private String IdName;
    private Class ClassName;
    private SimpleJdbcInsert simpleJdbcInsert;
    public JdbcTemplate jdbcTemplate;
    private boolean ReturnKey;

    // Constructor: would be called in subclass by using keyword:super;
    public JdbcCrudDao(DataSource dataSource, String TableName, String IdName, Class ClassName, boolean ReturnKey) {
        this.TableName = TableName;
        this.IdName = IdName;
        this.ClassName = ClassName;
        this.ReturnKey = ReturnKey;
        this.jdbcTemplate = new JdbcTemplate(dataSource);

        if (ReturnKey) {
            this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(TableName).usingGeneratedKeyColumns(IdName);
        } else {
            this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(TableName);
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public E save(E entity) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(entity);
        try {
            if (ReturnKey) {
                Number newId = simpleJdbcInsert.executeAndReturnKey(parameterSource);
                entity.setId(newId.intValue());

            } else {
                simpleJdbcInsert.execute(parameterSource);
            }
        } catch (DuplicateKeyException e) {
            logger.debug(entity.getId() + " is already in " + TableName);
        } catch (InvalidDataAccessApiUsageException e) {
            logger.debug(TableName + " has no generated keys.");
        }
        return entity;
    }

    @Override
    public boolean deleteById(ID id) {
        return deleteById(IdName, id);
    }

    public boolean deleteById(String idName, ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID is null");
        }
        String deleteSql = "DELETE FROM " + TableName + " WHERE " + idName + " =?";
        int checker = jdbcTemplate.update(deleteSql, id);
        logger.info(String.valueOf(checker) + ":1 means successful deletion, 0 means unsucessful deletion ");
        return (checker == 1) ? true : false;
    }

    @Override
    public E findById(ID id) {
        return findById(IdName, id, false, ClassName);
    }

    public E findByIdForUpdate(ID id) {

        return findById(IdName, id, true, ClassName);
    }

    /**
     * @return an entity
     * @throws java.sql.SQLException     if sql execution failed
     * @throws ResourceNotFoundException if no entity is found in db
     */

    @SuppressWarnings("unchecked")
    public E findById(String idName, ID id, boolean forUpdate, Class clazz) {
        E quote = null;
        String selectSql = "SELECT * FROM " + TableName + " WHERE " + idName + " =?";

        if (forUpdate) {
            selectSql += " for update";
        }
        logger.info(selectSql);

        try {
            quote = (E) jdbcTemplate
                    .queryForObject(selectSql,
                            BeanPropertyRowMapper.newInstance(clazz), id);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("Can't find quote id:" + id, e);
        }
        if (quote == null) {
            throw new ResourceNotFoundException("Er:Resource not found");
        }
        return quote;
    }

    @Override
    public boolean existsById(ID id) {
        return existsById(IdName, id);
    }

    public boolean existsById(String idName, ID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID can't be null");
        }
        String selectSql = "SELECT count(*) FROM " + TableName + " WHERE " + idName + " =?";
        int checker = jdbcTemplate.queryForObject(selectSql, Integer.class, id);
        logger.info(String.valueOf(checker) + ":number means exist, 0 means doesnot exists");
        return (checker != 0) ? true : false;
    }

}