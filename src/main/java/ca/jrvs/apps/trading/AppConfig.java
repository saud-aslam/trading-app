/*package ca.jrvs.apps.trading;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class AppConfig {

    private Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Value("${iex.host}")
    private String iex_host;

    @Bean
    public PlatformTransactionManager txManager(DataSource dataSource) {
    }

    @Bean
    public MarketDataConfig marketDataConfig() {
     MarketDataConfig marketDataConfig = new MarketDataConfig();
        marketDataConfig.setHost("https://cloud.iexapis.com/");
        marketDataConfig.setToken("pk_22792cfad91547bb99f9c84f1c5041e2");
        return marketDataConfig;
    }

    @Bean
    public DataSource dataSource() {
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

 */