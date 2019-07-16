package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import com.google.common.base.Joiner;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.dao.DataRetrievalFailureException;

import java.io.IOException;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;

public class ConnectionManager {

    private final String BATCH_QUOTE_URL;
    private HttpClientConnectionManager httpClientConnectionManager;

    //Constructor
    public ConnectionManager(HttpClientConnectionManager httpClientConnectionManager, MarketDataConfig marketDataConfig) {
        this.httpClientConnectionManager = httpClientConnectionManager;
        BATCH_QUOTE_URL = marketDataConfig.getHost() + "/stock/market/batch?symbols=%s&types=quote&token=" + marketDataConfig.getToken();
    }

    public static void main(String[] args) {

        HttpClientConnectionManager httpClientConnectionManager=httpClientConnectionManager(20, 100);
        MarketDataConfig marketDataConfig= new MarketDataConfig();
        marketDataConfig.setHost("https://cloud.iexapis.com/");
        marketDataConfig.setToken("pk_22792cfad91547bb99f9c84f1c5041e2");
        ConnectionManager connectionManager= new ConnectionManager(httpClientConnectionManager,marketDataConfig);
        List<String> symbols= Arrays.asList("aapl","fb");
        String string = Joiner.on(",").join(symbols);



        // print string
        System.out.println(string);


        System.out.println(connectionManager.parseResponseBody(connectionManager.getHttpClientAndResponseAfterExecution("https://cloud.iexapis.com/stable/stock/market/batch?symbols=%s&types=quote&token=pk_22792cfad91547bb99f9c84f1c5041e2")));
    }

    public static HttpClientConnectionManager httpClientConnectionManager(int min, int max) {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(max);
        cm.setDefaultMaxPerRoute(min);
        return cm;
    }

    public CloseableHttpClient getClientFromPool() {
        return HttpClients.custom().setConnectionManager(httpClientConnectionManager).setConnectionManagerShared(true)
                .build();

    }

    public CloseableHttpResponse getHttpClientAndResponseAfterExecution(String uri) {
        try (CloseableHttpClient httpClient = getClientFromPool()) {
            CloseableHttpResponse response = httpClient.execute(new HttpGet(uri));
            return response;
        } catch (IOException e) {
            throw new DataRetrievalFailureException("Data Retrieval Failure", e);
        }
    }

    public String parseResponseBody(CloseableHttpResponse response) {
        int status = response.getStatusLine().getStatusCode();
        switch (status) {
            case 200:
                try {
                    return EntityUtils.toString(response.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            default:
                throw new DataRetrievalFailureException("Error" + status);
        }

    }

}