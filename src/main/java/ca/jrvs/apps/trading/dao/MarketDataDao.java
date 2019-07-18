package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.model.dto.IexQuote;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static ca.jrvs.apps.trading.util.JsonUtil.toObjectFromJson;

@Repository
public class MarketDataDao {

    private final String BATCH_BASE_URL;
    private final String BATCH_TOKEN_URL;
    private HttpClientConnectionManager httpClientConnectionManager;

    //Constructor
    @Autowired
    public MarketDataDao(HttpClientConnectionManager httpClientConnectionManager, MarketDataConfig marketDataConfig) {
        this.httpClientConnectionManager = httpClientConnectionManager;
        BATCH_BASE_URL = marketDataConfig.getHost() + "/stable/stock/market/batch?symbols=";
        BATCH_TOKEN_URL = "&types=quote&token=" + marketDataConfig.getToken();

    }


    public List<IexQuote> UnmarshallJson(List<String> symbols) {

        String symbolsInString = symbols.stream().map(String::valueOf).collect(Collectors.joining(","));
        StringBuilder url = new StringBuilder();
        url.append(BATCH_BASE_URL).append(symbolsInString).append(BATCH_TOKEN_URL);
        String resp = parseResponseBody(getHttpClientAndResponseAfterExecution(url.toString()));

        JSONObject obj = new JSONObject(resp);
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
        System.out.println(iexQuotes);
        return iexQuotes;

    }

    public IexQuote UnmarshallJson(String symbol) {
        List<IexQuote> quotes = UnmarshallJson(Arrays.asList(symbol));
        if (quotes == null || quotes.size() != 1) {
            throw new DataRetrievalFailureException("Unable to get data");
        }
        return quotes.get(0);

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