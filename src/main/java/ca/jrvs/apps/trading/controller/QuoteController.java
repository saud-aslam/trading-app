
package ca.jrvs.apps.trading.controller;

import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.dto.IexQuote;
import ca.jrvs.apps.trading.model.dto.Quote;
import ca.jrvs.apps.trading.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Controller
@RequestMapping("/quote")
public class QuoteController {

    private QuoteService quoteService;
    private QuoteDao quoteDao;
    private MarketDataDao marketDataDao;

    @Autowired
    public QuoteController(QuoteService quoteService, QuoteDao quoteDao,
                           MarketDataDao marketDataDao) {
        this.quoteService = quoteService;
        this.quoteDao = quoteDao;
        this.marketDataDao = marketDataDao;
    }

    @PutMapping(path = "/iexMarketData")
    @ResponseStatus(HttpStatus.OK)
    public void updateMarketData() {
        try {
            quoteService.updateMarketData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping(path = "/")
    @ResponseStatus(HttpStatus.OK)
    public void putQuote(@RequestBody Quote quote) {
        try {
            quoteDao.update(Collections.singletonList(quote));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(path = "/tickerId/{tickerId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createQuote(@PathVariable String tickerId) {
        try {
            quoteService.initQuote(tickerId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @GetMapping(path = "/dailyList")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<Quote> getDailyList() {
        try {
            return quoteDao.findAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(path = "/iex/ticker/{ticker}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public IexQuote getQuote(@PathVariable String ticker) {
        try {
            return marketDataDao.UnmarshallJson(ticker);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(path = "/iex/tickersInBatch/{ticker}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<IexQuote> getBatchQuote(@PathVariable String ticker) {
        try {
            List<String> tickers = new ArrayList<String>(Arrays.asList(ticker.split(",")));

            return marketDataDao.UnmarshallJson(tickers);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}





