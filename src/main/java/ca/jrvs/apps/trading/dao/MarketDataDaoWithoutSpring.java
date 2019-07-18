package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.model.dto.IexQuote;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.dao.DataRetrievalFailureException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static ca.jrvs.apps.trading.util.JsonUtil.toObjectFromJson;

public class MarketDataDaoWithoutSpring {

    private final String BATCH_QUOTE_URL;
    private HttpClientConnectionManager httpClientConnectionManager;

    //Constructor
    public MarketDataDaoWithoutSpring(HttpClientConnectionManager httpClientConnectionManager, MarketDataConfig marketDataConfig) {
        this.httpClientConnectionManager = httpClientConnectionManager;
        BATCH_QUOTE_URL = marketDataConfig.getHost() + "/stock/market/batch?symbols=%s&types=quote&token=" + marketDataConfig.getToken();
    }

    public static void main(String[] args) {

        HttpClientConnectionManager httpClientConnectionManager = httpClientConnectionManager(20, 100);
        MarketDataConfig marketDataConfig = new MarketDataConfig();
        marketDataConfig.setHost("https://cloud.iexapis.com/");
        marketDataConfig.setToken("pk_22792cfad91547bb99f9c84f1c5041e2");
        MarketDataDaoWithoutSpring connectionManager = new MarketDataDaoWithoutSpring(httpClientConnectionManager, marketDataConfig);

        List<String> symbols = Arrays.asList("aal", "fb");
        //Join List of Strings to a String
        String symbolsInString = symbols.stream().map(String::valueOf).collect(Collectors.joining(","));
        //appending url pk_22792cfad91547bb99f9c84f1c5041e2
        StringBuilder url = new StringBuilder();
        url.append(marketDataConfig.getHost() + "stable/stock/market/batch?symbols=").append(symbolsInString).append("&types=quote&token=" + marketDataConfig.getToken());
        String resp = connectionManager.parseResponseBody(connectionManager.getHttpClientAndResponseAfterExecution(url.toString()));
        UnmarshallJson(resp);
        System.out.println(resp);
    }

    public static HttpClientConnectionManager httpClientConnectionManager(int min, int max) {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(max);
        cm.setDefaultMaxPerRoute(min);
        return cm;
    }

    public static void UnmarshallJson(String JsonResponse) {
        JSONObject obj = new JSONObject(JsonResponse);
        List<IexQuote> iexQuotes = new ArrayList<>();
        Iterator<String> SymbolsList = obj.keys(); //tickers extract
        while (SymbolsList.hasNext()) {
            // System.out.print(SymbolsList.next() + " ");
            String quote = (obj.getJSONObject(SymbolsList.next())).get("quote").toString();
            try {
                IexQuote iexQuote = toObjectFromJson(quote, IexQuote.class);
                iexQuotes.add(iexQuote);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // JSONArray quote = obj.getJSONArray("AAL");
        System.out.println("IexQUOTE: " + iexQuotes);
        // System.out.println(quote);

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
                throw new DataRetrievalFailureException("Error Status: " + status);
        }

    }

}


