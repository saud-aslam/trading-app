package ca.jrvs.apps.trading.dao;

@Repository
public class TraderDao implements CrudRepository<Trader, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(TraderDao.class);

    private final String TABLE_NAME = "trader";
    private final String ID_COLUMN = "id";

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleInsert;

    @Autowired
    public TraderDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(ID_COLUMN);
    }

    @Override
    public Trader save(Trader entity) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(entity);
        Number newId = simpleInsert.executeAndReturnKey(parameterSource);
        entity.setId(newId.intValue());
        return entity;
    }

    @Override
    public Trader findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID can't be null");
        }
        Trader trader = null;
        try {
            trader = jdbcTemplate
                    .queryForObject("select * from " + TABLE_NAME + " where id = ?",
                            BeanPropertyRowMapper.newInstance(Trader.class), id);
        } catch (EmptyResultDataAccessException e) {
            logger.debug("Can't find trader id:" + id, e);
        }
        return trader;
    }

    @Override
    public boolean existsById(Integer id) {
    }

    @Override
    public void deleteById(Integer id) {
    }
}