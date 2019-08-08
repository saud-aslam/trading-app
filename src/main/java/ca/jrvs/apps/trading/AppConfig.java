package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.util.StringUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class AppConfig {

    private Logger logger = LoggerFactory.getLogger(AppConfig.class);

    // @Value("${iex.host}")
    // private String iex_host;

  /*  @Bean
    public PlatformTransactionManager txManager(DataSource dataSource) {
    }

   */

    @Bean
    public MarketDataConfig marketDataConfig() {
        MarketDataConfig marketDataConfig = new MarketDataConfig();
        marketDataConfig.setHost("https://cloud.iexapis.com/");
        marketDataConfig.setToken(System.getenv("IEX_PUB_TOKEN"));
        return marketDataConfig;
    }

    @Bean
    public DataSource dataSource() {
        String jdbcUrl;
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        if (!StringUtil.isEmpty(System.getenv("RDS_HOSTNAME"))) {
            jdbcUrl = "jdbc:postgresql://" + System.getenv("RDS_HOSTNAME") + ":" + System.getenv("RDS_PORT") + "/jrvstrading";
            dataSource.setUsername(System.getenv("RDS_USERNAME"));
            dataSource.setPassword(System.getenv("RDS_PASSWORD"));
            dataSource.setUrl(jdbcUrl);
        } else {
            dataSource.setUrl(System.getenv("PSQL_URL"));
            dataSource.setUsername(System.getenv("PSQL_USER"));
            dataSource.setPassword(System.getenv("PSQL_PASSWORD"));
        }

        return dataSource;
    }

    //http://bit.ly/2tWTmzQ connectionPool
    @Bean
    public HttpClientConnectionManager httpClientConnectionManager() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(50);
        cm.setDefaultMaxPerRoute(50);
        return cm;
    }
}

