package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Test;

import java.util.Arrays;

public class MarketDataDaoTest {
    @Test
    public void UnmarshallJson() {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        MarketDataConfig marketDataConfig = new MarketDataConfig();
        marketDataConfig.setHost("https://cloud.iexapis.com/");
        marketDataConfig.setToken("pk_22792cfad91547bb99f9c84f1c5041e2");
        MarketDataDao dao = new MarketDataDao(cm, marketDataConfig);
        dao.UnmarshallJson(Arrays.asList("aal", "fb"));
        dao.UnmarshallJson("aal");

    }

}